package com.ruoyi.school.service.impl;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.school.mapper.StuSelectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.school.mapper.StuClassMapper;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StuClassServiceImpl implements IStuClassService
{
    @Autowired
    private StuClassMapper stuClassMapper;

    @Autowired
    private StuSelectionMapper stuSelectionMapper;

    @Override
    public List<StuClass> selectStuClassList(StuClass stuClass) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!SecurityUtils.isAdmin(loginUser.getUserId())) {
            boolean isTeacher = loginUser.getUser().getRoles().stream()
                    .anyMatch(role -> "teacher".equals(role.getRoleKey()));
            if (isTeacher) {
                stuClass.setStaffId(loginUser.getUsername());
            }
        }
        return stuClassMapper.selectStuClassList(stuClass);
    }

    @Override
    @Transactional
    public void executeRandomKick() {
        // 1. 先查出哪些选课记录是超额的
        List<Long> overIds = stuSelectionMapper.selectOverCapacitySelectionIds();

        // 2. 如果有超额的，就执行删除
        if (overIds != null && overIds.size() > 0) {
            // 调用批量删除。触发器会在这里逐一触发，更新 stu_class 的人数
            stuSelectionMapper.deleteStuSelectionBySelectionIds(overIds.toArray(new Long[0]));
        }
    }

    @Override
    public int insertStuClass(StuClass stuClass) {
        if (stuClass.getStaffId() == null || stuClass.getStaffId().isEmpty()) {
            stuClass.setStaffId(SecurityUtils.getUsername());
        }
        if (stuClassMapper.existsDuplicateStuClass(stuClass) > 0) {
            throw new ServiceException("开课失败：已存在相同的开课记录");
        }
        // 自动生成唯一的 classSection（班级号）
        // 规则：同一【课程 + 学期】下，主课班级号全局唯一（不区分教师）；追加时段继承主课班级号。
        if (stuClass.getIsPrimary() != null && stuClass.getIsPrimary() == 1) {
            Integer maxNo = stuClassMapper.selectMaxPrimaryClassSectionNo(stuClass.getCourseId(), stuClass.getSemester());
            int nextNo = (maxNo == null ? 0 : maxNo) + 1;
            String candidate = String.format("%02d班", nextNo);
            // 避免历史数据/并发导致的冲突：若已存在则递增直到可用
            while (stuClassMapper.existsPrimaryClassSection(stuClass.getCourseId(), stuClass.getSemester(), candidate, null) > 0) {
                nextNo++;
                candidate = String.format("%02d班", nextNo);
            }
            stuClass.setClassSection(candidate);
        }
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("排课失败：该时段内已有课程安排！");
        }
        return stuClassMapper.insertStuClass(stuClass);
    }

    @Override
    public int updateStuClass(StuClass stuClass) {
        if (stuClassMapper.existsDuplicateStuClass(stuClass) > 0) {
            throw new ServiceException("修改失败：已存在相同的开课记录");
        }
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("修改失败：存在排课冲突！");
        }
        return stuClassMapper.updateStuClass(stuClass);
    }

    @Override
    @Transactional
    public int deleteStuClassByClassIds(Long[] classIds) {
        for (Long id : classIds) {
            StuClass current = stuClassMapper.selectStuClassByClassId(id);
            if (current != null && current.getIsPrimary() != null && current.getIsPrimary() == 1) {
                StuClass childrenQuery = new StuClass();
                childrenQuery.setCourseId(current.getCourseId());
                childrenQuery.setStaffId(current.getStaffId());
                childrenQuery.setSemester(current.getSemester());
                childrenQuery.setClassSection(current.getClassSection());
                childrenQuery.setIsPrimary(0);

                List<StuClass> children = stuClassMapper.selectStuClassList(childrenQuery);
                if (children != null && children.size() > 0) {
                    Long[] childIds = children.stream().map(StuClass::getClassId).toArray(Long[]::new);
                    stuClassMapper.deleteStuClassByClassIds(childIds);
                }
            }
        }
        return stuClassMapper.deleteStuClassByClassIds(classIds);
    }

    @Override
    public int deleteStuClassByClassId(Long classId) { return stuClassMapper.deleteStuClassByClassId(classId); }

    @Override
    public StuClass selectStuClassByClassId(Long classId) { return stuClassMapper.selectStuClassByClassId(classId); }

    @Override
    public List<Map<String, Object>> selectAllCourseOptions() { return stuClassMapper.selectAllCourseOptions(); }

    @Override
    public List<Map<String, Object>> selectAllClassroomOptions() { return stuClassMapper.selectAllClassroomOptions(); }
}
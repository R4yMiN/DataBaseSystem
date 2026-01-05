package com.ruoyi.school.service.impl;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.school.mapper.StuClassMapper;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 开课Service业务层处理
 */
@Service
public class StuClassServiceImpl implements IStuClassService
{
    @Autowired
    private StuClassMapper stuClassMapper;

    /**
     * 查询开课列表（带权限过滤）
     */
    @Override
    public List<StuClass> selectStuClassList(StuClass stuClass)
    {
        // 1. 获取当前登录用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();

        // 2. 权限过滤：如果当前用户【不是管理员】，则进行数据过滤
        if (!SecurityUtils.isAdmin(loginUser.getUserId())) {

            // 判断是否是教师角色 (roleKey 为 teacher)
            boolean isTeacher = loginUser.getUser().getRoles().stream()
                    .anyMatch(role -> "teacher".equals(role.getRoleKey()));

            if (isTeacher) {
                // 如果是老师，强制只能看 staff_id 等于自己账号名的数据
                // 这里直接拿 loginUser.getUsername()，绕开了找不到 StuTeacher 的问题
                stuClass.setStaffId(loginUser.getUsername());
            }
        }

        // 3. 执行查询。如果是管理员，则不进上面的 if，直接查全表
        return stuClassMapper.selectStuClassList(stuClass);
    }

    /**
     * 新增开课
     */
    @Override
    public int insertStuClass(StuClass stuClass) {
        // 1. 自动填充工号（从当前登录账号获取）
        if (stuClass.getStaffId() == null || stuClass.getStaffId().isEmpty()) {
            stuClass.setStaffId(SecurityUtils.getUsername());
        }

        // 1.1 防止重复提交：同条件记录已存在则直接拦截
        if (stuClassMapper.existsDuplicateStuClass(stuClass) > 0) {
            throw new ServiceException("开课失败：已存在相同的开课记录（请勿重复提交）");
        }

        // 2. 【核心逻辑】如果是老师申请的主课程，自动编排班级号
        if (stuClass.getIsPrimary() != null && stuClass.getIsPrimary() == 1) {
            // 统计该课程在当前学期已经开了多少个班
            StuClass countQuery = new StuClass();
            countQuery.setCourseId(stuClass.getCourseId());
            countQuery.setSemester(stuClass.getSemester());
            countQuery.setIsPrimary(1);
            // 【关键修改】：这里绝对不要写 countQuery.setStaffId(...) ！！！

            int count = stuClassMapper.selectStuClassList(countQuery).size();
            stuClass.setClassSection(String.format("%02d班", count + 1));
        }

        // 2. 数字化冲突校验（保留你原有的逻辑）
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("排课失败：该时段内已有课程安排！");
        }
        return stuClassMapper.insertStuClass(stuClass);
    }

    /**
     * 修改开课
     */
    @Override
    public int updateStuClass(StuClass stuClass) {
        // 修改时也做一次重复判断（避免把两条改成一模一样）
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
                // 【核心逻辑】如果是主课程，找出该班级所有的追加时段并一并删除
                StuClass childrenQuery = new StuClass();
                childrenQuery.setCourseId(current.getCourseId());
                childrenQuery.setStaffId(current.getStaffId());
                childrenQuery.setSemester(current.getSemester());
                childrenQuery.setClassSection(current.getClassSection());
                childrenQuery.setIsPrimary(0); // 找追加时段

                List<StuClass> children = stuClassMapper.selectStuClassList(childrenQuery);
                if (children != null && children.size() > 0) {
                    Long[] childIds = children.stream().map(StuClass::getClassId).toArray(Long[]::new);
                    stuClassMapper.deleteStuClassByClassIds(childIds);
                }
            }
        }
        // 最后删除选中的记录本身
        return stuClassMapper.deleteStuClassByClassIds(classIds);
    }

    @Override
    public int deleteStuClassByClassId(Long classId) {
        return stuClassMapper.deleteStuClassByClassId(classId);
    }

    @Override
    public StuClass selectStuClassByClassId(Long classId) {
        return stuClassMapper.selectStuClassByClassId(classId);
    }

    @Override
    public void executeRandomKick() {
        stuClassMapper.executeRandomKick();
    }

    @Override
    public List<Map<String, Object>> selectAllCourseOptions() {
        return stuClassMapper.selectAllCourseOptions();
    }

    @Override
    public List<Map<String, Object>> selectAllClassroomOptions() {
        return stuClassMapper.selectAllClassroomOptions();
    }
}
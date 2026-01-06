package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.mapper.StuClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.school.mapper.StuSelectionMapper;
import com.ruoyi.school.domain.StuSelection;
import com.ruoyi.school.service.IStuSelectionService;

@Service
public class StuSelectionServiceImpl implements IStuSelectionService
{
    @Autowired
    private StuSelectionMapper stuSelectionMapper;

    @Autowired
    private StuClassMapper stuClassMapper;

    @Override
    public StuSelection selectStuSelectionBySelectionId(Long selectionId) {
        return stuSelectionMapper.selectStuSelectionBySelectionId(selectionId);
    }

    @Override
    public List<StuSelection> selectStuSelectionList(StuSelection stuSelection) {
        return stuSelectionMapper.selectStuSelectionList(stuSelection);
    }

    @Override
    public List<StuSelection> selectMySchedule(String studentId) {
        return stuSelectionMapper.selectMySchedule(studentId);
    }

    @Override
    @Transactional
    public int insertStuSelection(StuSelection stuSelection) {
        // 1. 获取要选的那门主课信息
        StuClass parent = stuClassMapper.selectStuClassByClassId(stuSelection.getClassId());
        if (parent == null) {
            throw new ServiceException("选课失败：课程不存在");
        }

        // 2. 【核心校验】如果当前人数 >= 容量，直接拦截（实现先到先得）
        if (parent.getSelectedNum() != null && parent.getSelectedNum() >= parent.getCapacity()) {
            throw new ServiceException("选课失败：该课程人数已满，请选择其他班级");
        }

        // 3. 查出这门课的所有关联项（包括追加时段）
        StuClass query = new StuClass();
        query.setCourseId(parent.getCourseId());
        query.setStaffId(parent.getStaffId());
        query.setClassSection(parent.getClassSection());
        query.setSemester(parent.getSemester());
        List<StuClass> allParts = stuClassMapper.selectStuClassList(query);

        // 4. 执行插入（数据库触发器会自动给 selected_num 加 1）
        int count = 0;
        for (StuClass part : allParts) {
            if (stuSelectionMapper.checkAlreadySelected(stuSelection.getStudentId(), part.getClassId()) == 0) {
                StuSelection s = new StuSelection();
                s.setStudentId(stuSelection.getStudentId());
                s.setClassId(part.getClassId());
                count += stuSelectionMapper.insertStuSelection(s);
            }
        }
        return count;
    }

    @Override
    public int updateStuSelection(StuSelection stuSelection) {
        return stuSelectionMapper.updateStuSelection(stuSelection);
    }

    @Override
    @Transactional
    public int deleteStuSelectionBySelectionIds(Long[] selectionIds) {
        // 数据库触发器会自动给 selected_num 减 1
        return stuSelectionMapper.deleteStuSelectionBySelectionIds(selectionIds);
    }

    @Override
    public int deleteStuSelectionBySelectionId(Long selectionId) {
        return stuSelectionMapper.deleteStuSelectionBySelectionId(selectionId);
    }

    @Override
    public List<StuSelection> selectStudentsByClassId(Long classId) {
        return stuSelectionMapper.selectStudentsByClassId(classId);
    }
}
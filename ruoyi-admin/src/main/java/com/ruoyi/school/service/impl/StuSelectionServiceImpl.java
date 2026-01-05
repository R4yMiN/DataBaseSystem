package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.mapper.StuClassMapper; // 必须有这一行！
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
    private StuClassMapper stuClassMapper; // 确保变量名是 stuClassMapper

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
        // 1. 获取选中的那行课（父亲）
        StuClass parent = stuClassMapper.selectStuClassByClassId(stuSelection.getClassId());

        // 2. 查出这门课的所有关联项（包括父亲自己和所有儿子）
        StuClass query = new StuClass();
        query.setCourseId(parent.getCourseId());
        query.setStaffId(parent.getStaffId());
        query.setClassSection(parent.getClassSection());
        query.setSemester(parent.getSemester());
        List<StuClass> allParts = stuClassMapper.selectStuClassList(query);

        // 3. 循环插入选课表
        int count = 0;
        for (StuClass part : allParts) {
            StuSelection s = new StuSelection();
            s.setStudentId(stuSelection.getStudentId());
            s.setClassId(part.getClassId());
            // 这里的 checkAlreadySelected 记得按之前教你的方式传参
            if (stuSelectionMapper.checkAlreadySelected(s.getStudentId(), s.getClassId()) == 0) {
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
    public int deleteStuSelectionBySelectionIds(Long[] selectionIds)
    {
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
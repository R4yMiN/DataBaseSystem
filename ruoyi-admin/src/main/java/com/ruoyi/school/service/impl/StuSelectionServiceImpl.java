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
    @Transactional
    public int insertStuSelection(StuSelection stuSelection)
    {
        StuClass targetClass = stuClassMapper.selectStuClassByClassId(stuSelection.getClassId());

        if (targetClass.getSelectedNum() >= targetClass.getCapacity()) {
            throw new ServiceException("选课失败：人数已满");
        }

        if (stuSelectionMapper.checkAlreadySelected(stuSelection.getStudentId(), stuSelection.getClassId()) > 0) {
            throw new ServiceException("重复选课！");
        }

        int conflict = stuSelectionMapper.checkStudentTimeConflict(
                stuSelection.getStudentId(),
                targetClass.getDayOfWeek(),
                targetClass.getLessonStart(),
                targetClass.getLessonEnd(),
                targetClass.getWeekStart(),
                targetClass.getWeekEnd(),
                targetClass.getWeekType()
        );
        if (conflict > 0) {
            throw new ServiceException("时间冲突！");
        }

        int rows = stuSelectionMapper.insertStuSelection(stuSelection);

        // 重点：调用上面接口里定义的 plusSelectedNum
        if (rows > 0) {
            stuClassMapper.plusSelectedNum(stuSelection.getClassId());
        }
        return rows;
    }

    @Override
    public int updateStuSelection(StuSelection stuSelection) {
        return stuSelectionMapper.updateStuSelection(stuSelection);
    }

    @Override
    @Transactional
    public int deleteStuSelectionBySelectionIds(Long[] selectionIds)
    {
        for (Long id : selectionIds) {
            StuSelection s = stuSelectionMapper.selectStuSelectionBySelectionId(id);
            if (s != null) {
                // 重点：调用上面接口里定义的 minusSelectedNum
                stuClassMapper.minusSelectedNum(s.getClassId());
            }
        }
        return stuSelectionMapper.deleteStuSelectionBySelectionIds(selectionIds);
    }

    @Override
    public int deleteStuSelectionBySelectionId(Long selectionId) {
        return stuSelectionMapper.deleteStuSelectionBySelectionId(selectionId);
    }
}
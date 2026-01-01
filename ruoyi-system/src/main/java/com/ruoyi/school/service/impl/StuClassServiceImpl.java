package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuClassMapper;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;

/**
 * 开课Service业务层处理
 */
@Service
public class StuClassServiceImpl implements IStuClassService
{
    @Autowired
    private StuClassMapper stuClassMapper;

    @Override
    public StuClass selectStuClassByClassId(Long classId) {
        return stuClassMapper.selectStuClassByClassId(classId);
    }

    @Override
    public List<StuClass> selectStuClassList(StuClass stuClass) {
        return stuClassMapper.selectStuClassList(stuClass);
    }

    /**
     * 新增开课：增加数字化冲突校验
     */
    @Override
    public int insertStuClass(StuClass stuClass) {
        // 1. 调用冲突检查（周次、星期、节次、教室、老师）
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("排课失败：该时间段内教室或老师已有课程安排！");
        }
        return stuClassMapper.insertStuClass(stuClass);
    }

    /**
     * 修改开课：增加数字化冲突校验
     */
    @Override
    public int updateStuClass(StuClass stuClass) {
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("修改失败：调整后的时间与现有课程冲突！");
        }
        return stuClassMapper.updateStuClass(stuClass);
    }

    /**
     * 批量删除开课
     */
    @Override
    public int deleteStuClassByClassIds(Long[] classIds) {
        return stuClassMapper.deleteStuClassByClassIds(classIds);
    }

    /**
     * 删除单条开课信息 (之前漏掉的就是这个方法)
     */
    @Override
    public int deleteStuClassByClassId(Long classId) {
        return stuClassMapper.deleteStuClassByClassId(classId);
    }

    /**
     * 随机抽签逻辑
     */
    @Override
    public void executeRandomKick() {
        stuClassMapper.executeRandomKick();
    }
}
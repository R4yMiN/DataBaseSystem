package com.ruoyi.school.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.school.mapper.StuClassMapper;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;

/**
 * 开课Service业务层处理
 */
@Service // <--- 必须有这一行，否则报错！
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

    @Override
    public int insertStuClass(StuClass stuClass) {
        // 数字化冲突校验
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("排课失败：该时段内已有课程安排！");
        }
        return stuClassMapper.insertStuClass(stuClass);
    }

    @Override
    public int updateStuClass(StuClass stuClass) {
        if (stuClassMapper.checkSchedulingConflict(stuClass) > 0) {
            throw new ServiceException("修改失败：存在排课冲突！");
        }
        return stuClassMapper.updateStuClass(stuClass);
    }

    @Override
    public int deleteStuClassByClassIds(Long[] classIds) {
        return stuClassMapper.deleteStuClassByClassIds(classIds);
    }

    @Override
    public int deleteStuClassByClassId(Long classId) {
        return stuClassMapper.deleteStuClassByClassId(classId);
    }

    @Override
    public void executeRandomKick() {
        stuClassMapper.executeRandomKick();
    }

    /**
     * 这里就是你刚才添加的功能
     */
    @Override
    public List<Map<String, Object>> selectAllCourseOptions() {
        return stuClassMapper.selectAllCourseOptions();
    }
}
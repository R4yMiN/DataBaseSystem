package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuClassMapper;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;

/**
 * 开课Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-30
 */
@Service
public class StuClassServiceImpl implements IStuClassService 
{
    @Autowired
    private StuClassMapper stuClassMapper;

    /**
     * 查询开课
     * 
     * @param classId 开课主键
     * @return 开课
     */
    @Override
    public StuClass selectStuClassByClassId(Long classId)
    {
        return stuClassMapper.selectStuClassByClassId(classId);
    }

    /**
     * 查询开课列表
     * 
     * @param stuClass 开课
     * @return 开课
     */
    @Override
    public List<StuClass> selectStuClassList(StuClass stuClass)
    {
        return stuClassMapper.selectStuClassList(stuClass);
    }

    /**
     * 新增开课
     * 
     * @param stuClass 开课
     * @return 结果
     */
    @Override
    public int insertStuClass(StuClass stuClass)
    {
        return stuClassMapper.insertStuClass(stuClass);
    }

    /**
     * 修改开课
     * 
     * @param stuClass 开课
     * @return 结果
     */
    @Override
    public int updateStuClass(StuClass stuClass)
    {
        return stuClassMapper.updateStuClass(stuClass);
    }

    /**
     * 批量删除开课
     * 
     * @param classIds 需要删除的开课主键
     * @return 结果
     */
    @Override
    public int deleteStuClassByClassIds(Long[] classIds)
    {
        return stuClassMapper.deleteStuClassByClassIds(classIds);
    }

    /**
     * 删除开课信息
     * 
     * @param classId 开课主键
     * @return 结果
     */
    @Override
    public int deleteStuClassByClassId(Long classId)
    {
        return stuClassMapper.deleteStuClassByClassId(classId);
    }
}

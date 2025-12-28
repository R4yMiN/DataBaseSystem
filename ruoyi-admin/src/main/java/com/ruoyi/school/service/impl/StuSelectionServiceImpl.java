package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuSelectionMapper;
import com.ruoyi.school.domain.StuSelection;
import com.ruoyi.school.service.IStuSelectionService;

/**
 * 选课成绩Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class StuSelectionServiceImpl implements IStuSelectionService 
{
    @Autowired
    private StuSelectionMapper stuSelectionMapper;

    /**
     * 查询选课成绩
     * 
     * @param selectionId 选课成绩主键
     * @return 选课成绩
     */
    @Override
    public StuSelection selectStuSelectionBySelectionId(Long selectionId)
    {
        return stuSelectionMapper.selectStuSelectionBySelectionId(selectionId);
    }

    /**
     * 查询选课成绩列表
     * 
     * @param stuSelection 选课成绩
     * @return 选课成绩
     */
    @Override
    public List<StuSelection> selectStuSelectionList(StuSelection stuSelection)
    {
        return stuSelectionMapper.selectStuSelectionList(stuSelection);
    }

    /**
     * 新增选课成绩
     * 
     * @param stuSelection 选课成绩
     * @return 结果
     */
    @Override
    public int insertStuSelection(StuSelection stuSelection)
    {
        return stuSelectionMapper.insertStuSelection(stuSelection);
    }

    /**
     * 修改选课成绩
     * 
     * @param stuSelection 选课成绩
     * @return 结果
     */
    @Override
    public int updateStuSelection(StuSelection stuSelection)
    {
        return stuSelectionMapper.updateStuSelection(stuSelection);
    }

    /**
     * 批量删除选课成绩
     * 
     * @param selectionIds 需要删除的选课成绩主键
     * @return 结果
     */
    @Override
    public int deleteStuSelectionBySelectionIds(Long[] selectionIds)
    {
        return stuSelectionMapper.deleteStuSelectionBySelectionIds(selectionIds);
    }

    /**
     * 删除选课成绩信息
     * 
     * @param selectionId 选课成绩主键
     * @return 结果
     */
    @Override
    public int deleteStuSelectionBySelectionId(Long selectionId)
    {
        return stuSelectionMapper.deleteStuSelectionBySelectionId(selectionId);
    }
}

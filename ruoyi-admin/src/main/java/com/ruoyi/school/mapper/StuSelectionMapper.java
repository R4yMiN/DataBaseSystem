package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StuSelection;

/**
 * 选课成绩Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface StuSelectionMapper 
{
    /**
     * 查询选课成绩
     * 
     * @param selectionId 选课成绩主键
     * @return 选课成绩
     */
    public StuSelection selectStuSelectionBySelectionId(Long selectionId);

    /**
     * 查询选课成绩列表
     * 
     * @param stuSelection 选课成绩
     * @return 选课成绩集合
     */
    public List<StuSelection> selectStuSelectionList(StuSelection stuSelection);

    /**
     * 新增选课成绩
     * 
     * @param stuSelection 选课成绩
     * @return 结果
     */
    public int insertStuSelection(StuSelection stuSelection);

    /**
     * 修改选课成绩
     * 
     * @param stuSelection 选课成绩
     * @return 结果
     */
    public int updateStuSelection(StuSelection stuSelection);

    /**
     * 删除选课成绩
     * 
     * @param selectionId 选课成绩主键
     * @return 结果
     */
    public int deleteStuSelectionBySelectionId(Long selectionId);

    /**
     * 批量删除选课成绩
     * 
     * @param selectionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuSelectionBySelectionIds(Long[] selectionIds);
}

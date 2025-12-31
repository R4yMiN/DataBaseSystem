package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StuClass;

/**
 * 开课Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-30
 */
public interface StuClassMapper 
{
    /**
     * 查询开课
     * 
     * @param classId 开课主键
     * @return 开课
     */
    public StuClass selectStuClassByClassId(Long classId);

    /**
     * 查询开课列表
     * 
     * @param stuClass 开课
     * @return 开课集合
     */
    public List<StuClass> selectStuClassList(StuClass stuClass);

    /**
     * 新增开课
     * 
     * @param stuClass 开课
     * @return 结果
     */
    public int insertStuClass(StuClass stuClass);

    /**
     * 修改开课
     * 
     * @param stuClass 开课
     * @return 结果
     */
    public int updateStuClass(StuClass stuClass);

    /**
     * 删除开课
     * 
     * @param classId 开课主键
     * @return 结果
     */
    public int deleteStuClassByClassId(Long classId);

    /**
     * 批量删除开课
     * 
     * @param classIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuClassByClassIds(Long[] classIds);

    /**
     * 执行随机抽签剔除逻辑
     */
    public int executeRandomKick();
}

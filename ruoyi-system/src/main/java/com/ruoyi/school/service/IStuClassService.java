package com.ruoyi.school.service;

import java.util.List;
import com.ruoyi.school.domain.StuClass;

/**
 * 开课Service接口
 *
 * @author ruoyi
 * @date 2025-12-30
 */
public interface IStuClassService
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
     * 批量删除开课
     *
     * @param classIds 需要删除的开课主键集合
     * @return 结果
     */
    public int deleteStuClassByClassIds(Long[] classIds);

    /**
     * 删除开课信息
     *
     * @param classId 开课主键
     * @return 结果
     */
    public int deleteStuClassByClassId(Long classId);

    /**
     * 随机抽签剔除超员学生 (这一行是你需要添加的)
     */
    public void executeRandomKick();
}
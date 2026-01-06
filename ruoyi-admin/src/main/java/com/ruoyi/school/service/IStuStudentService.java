package com.ruoyi.school.service;

import java.util.List;
import com.ruoyi.school.domain.StuStudent;

/**
 * 学生Service接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface IStuStudentService 
{
    /**
     * 查询学生
     * 
     * @param studentId 学生主键
     * @return 学生
     */
    public StuStudent selectStuStudentByStudentId(String studentId);

    /**
     * 查询学生列表
     * 
     * @param stuStudent 学生
     * @return 学生集合
     */
    public List<StuStudent> selectStuStudentList(StuStudent stuStudent);

    /**
     * 新增学生
     * 
     * @param stuStudent 学生
     * @return 结果
     */
    public int insertStuStudent(StuStudent stuStudent);

    /**
     * 修改学生
     * 
     * @param stuStudent 学生
     * @return 结果
     */
    public int updateStuStudent(StuStudent stuStudent);

    /**
     * 批量删除学生
     * 
     * @param studentIds 需要删除的学生主键集合
     * @return 结果
     */
    public int deleteStuStudentByStudentIds(String[] studentIds);

    /**
     * 删除学生信息
     * 
     * @param studentId 学生主键
     * @return 结果
     */
    public int deleteStuStudentByStudentId(String studentId);
}

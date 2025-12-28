package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuStudentMapper;
import com.ruoyi.school.domain.StuStudent;
import com.ruoyi.school.service.IStuStudentService;

/**
 * 学生Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class StuStudentServiceImpl implements IStuStudentService 
{
    @Autowired
    private StuStudentMapper stuStudentMapper;

    /**
     * 查询学生
     * 
     * @param studentId 学生主键
     * @return 学生
     */
    @Override
    public StuStudent selectStuStudentByStudentId(String studentId)
    {
        return stuStudentMapper.selectStuStudentByStudentId(studentId);
    }

    /**
     * 查询学生列表
     * 
     * @param stuStudent 学生
     * @return 学生
     */
    @Override
    public List<StuStudent> selectStuStudentList(StuStudent stuStudent)
    {
        return stuStudentMapper.selectStuStudentList(stuStudent);
    }

    /**
     * 新增学生
     * 
     * @param stuStudent 学生
     * @return 结果
     */
    @Override
    public int insertStuStudent(StuStudent stuStudent)
    {
        return stuStudentMapper.insertStuStudent(stuStudent);
    }

    /**
     * 修改学生
     * 
     * @param stuStudent 学生
     * @return 结果
     */
    @Override
    public int updateStuStudent(StuStudent stuStudent)
    {
        return stuStudentMapper.updateStuStudent(stuStudent);
    }

    /**
     * 批量删除学生
     * 
     * @param studentIds 需要删除的学生主键
     * @return 结果
     */
    @Override
    public int deleteStuStudentByStudentIds(String[] studentIds)
    {
        return stuStudentMapper.deleteStuStudentByStudentIds(studentIds);
    }

    /**
     * 删除学生信息
     * 
     * @param studentId 学生主键
     * @return 结果
     */
    @Override
    public int deleteStuStudentByStudentId(String studentId)
    {
        return stuStudentMapper.deleteStuStudentByStudentId(studentId);
    }
}

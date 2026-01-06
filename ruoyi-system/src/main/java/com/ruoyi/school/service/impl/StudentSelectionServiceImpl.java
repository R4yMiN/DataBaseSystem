package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StudentSelectionMapper;
import com.ruoyi.school.domain.StudentSelection;
import com.ruoyi.school.service.IStudentSelectionService;

/**
 * 学生自主选课Service业务层处理
 *
 * @author admin
 * @date 2026-01-03
 */
@Service
public class StudentSelectionServiceImpl implements IStudentSelectionService
{
    @Autowired
    private StudentSelectionMapper studentSelectionMapper;

    /**
     * 查询学生自主选课
     *
     * @param id 学生自主选课主键
     * @return 学生自主选课
     */
    @Override
    public StudentSelection selectStudentSelectionById(Long id)
    {
        return studentSelectionMapper.selectStudentSelectionById(id);
    }

    /**
     * 查询学生自主选课列表
     *
     * @param studentSelection 学生自主选课
     * @return 学生自主选课
     */
    @Override
    public List<StudentSelection> selectStudentSelectionList(StudentSelection studentSelection)
    {
        return studentSelectionMapper.selectStudentSelectionList(studentSelection);
    }

    /**
     * 新增学生自主选课
     *
     * @param studentSelection 学生自主选课
     * @return 结果
     */
    @Override
    public int insertStudentSelection(StudentSelection studentSelection)
    {
        studentSelection.setCreateTime(DateUtils.getNowDate());
        return studentSelectionMapper.insertStudentSelection(studentSelection);
    }

    /**
     * 修改学生自主选课
     *
     * @param studentSelection 学生自主选课
     * @return 结果
     */
    @Override
    public int updateStudentSelection(StudentSelection studentSelection)
    {
        studentSelection.setUpdateTime(DateUtils.getNowDate());
        return studentSelectionMapper.updateStudentSelection(studentSelection);
    }

    /**
     * 选课
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    @Override
    public int selectCourse(Long id, String studentId, String studentName)
    {
        return studentSelectionMapper.selectCourse(id, studentId, studentName);
    }

    /**
     * 退选
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    @Override
    public int unselectCourse(Long id)
    {
        return studentSelectionMapper.unselectCourse(id);
    }

    /**
     * 批量删除学生自主选课
     *
     * @param ids 需要删除的学生自主选课主键
     * @return 结果
     */
    @Override
    public int deleteStudentSelectionByIds(Long[] ids)
    {
        return studentSelectionMapper.deleteStudentSelectionByIds(ids);
    }

    /**
     * 删除学生自主选课信息
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    @Override
    public int deleteStudentSelectionById(Long id)
    {
        return studentSelectionMapper.deleteStudentSelectionById(id);
    }

    /**
     * 检查时间冲突
     *
     * @param studentId 学生ID
     * @param semester 学期
     * @param classTime 上课时间
     * @param id 当前选课ID
     * @return 冲突数量
     */
    @Override
    public int checkTimeConflict(String studentId, String semester, String classTime, Long id)
    {
        return studentSelectionMapper.checkTimeConflict(studentId, semester, classTime, id);
    }

    /**
     * 查询学生已选课程列表
     *
     * @param studentId 学生ID
     * @return 已选课程列表
     */
    @Override
    public List<StudentSelection> selectMySchedule(String studentId)
    {
        return studentSelectionMapper.selectMySchedule(studentId);
    }
}
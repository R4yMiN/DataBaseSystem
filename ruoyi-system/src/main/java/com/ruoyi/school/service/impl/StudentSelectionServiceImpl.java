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
 * @date 2026-01-02
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
     * 学生选择课程
     */
    @Override
    public int selectCourse(Long id)
    {
        return studentSelectionMapper.selectCourse(id);
    }

    /**
     * 学生退选课程
     */
    @Override
    public int unselectCourse(Long id)
    {
        return studentSelectionMapper.unselectCourse(id);
    }
}

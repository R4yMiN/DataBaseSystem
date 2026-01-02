package com.ruoyi.school.service;

import java.util.List;
import com.ruoyi.school.domain.StudentSelection;

/**
 * 学生自主选课Service接口
 * 
 * @author admin
 * @date 2026-01-02
 */
public interface IStudentSelectionService 
{
    /**
     * 查询学生自主选课
     * 
     * @param id 学生自主选课主键
     * @return 学生自主选课
     */
    public StudentSelection selectStudentSelectionById(Long id);

    /**
     * 查询学生自主选课列表
     * 
     * @param studentSelection 学生自主选课
     * @return 学生自主选课集合
     */
    public List<StudentSelection> selectStudentSelectionList(StudentSelection studentSelection);

    /**
     * 新增学生自主选课
     * 
     * @param studentSelection 学生自主选课
     * @return 结果
     */
    public int insertStudentSelection(StudentSelection studentSelection);

    /**
     * 修改学生自主选课
     * 
     * @param studentSelection 学生自主选课
     * @return 结果
     */
    public int updateStudentSelection(StudentSelection studentSelection);

    /**
     * 批量删除学生自主选课
     * 
     * @param ids 需要删除的学生自主选课主键集合
     * @return 结果
     */
    public int deleteStudentSelectionByIds(Long[] ids);

    /**
     * 删除学生自主选课信息
     * 
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int deleteStudentSelectionById(Long id);

    /**
     * 学生选择课程（将 is_selected 置为 1，并更新人数/容量）
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int selectCourse(Long id);

    /**
     * 学生退选课程（将 is_selected 置为 0，并更新人数/容量）
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int unselectCourse(Long id);
}

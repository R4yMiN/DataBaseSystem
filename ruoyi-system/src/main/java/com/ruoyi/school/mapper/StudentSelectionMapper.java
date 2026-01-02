package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StudentSelection;

/**
 * 学生自主选课Mapper接口
 * 
 * @author admin
 * @date 2026-01-02
 */
public interface StudentSelectionMapper 
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
     * 删除学生自主选课
     * 
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int deleteStudentSelectionById(Long id);

    /**
     * 批量删除学生自主选课
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudentSelectionByIds(Long[] ids);

    /**
     * 选择课程（原子更新：is_selected、status、selected_num、remain_capacity）
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int selectCourse(Long id);

    /**
     * 退选课程（原子更新：is_selected、status、selected_num、remain_capacity）
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int unselectCourse(Long id);
}

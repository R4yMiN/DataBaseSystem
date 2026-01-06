package com.ruoyi.school.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.school.domain.StudentSelection;

/**
 * 学生自主选课Mapper接口
 * 
 * @author admin
 * @date 2026-01-03
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
     * 选课
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int selectCourse(@Param("id") Long id, @Param("studentId") String studentId, @Param("studentName") String studentName);

    /**
     * 退选
     *
     * @param id 学生自主选课主键
     * @return 结果
     */
    public int unselectCourse(@Param("id") Long id);

    /**
     * 检查时间冲突
     *
     * @param studentId 学生ID
     * @param semester 学期
     * @param classTime 上课时间
     * @param id 当前选课ID（排除自身）
     * @return 冲突数量
     */
    public int checkTimeConflict(@Param("studentId") String studentId, @Param("semester") String semester, 
                                  @Param("classTime") String classTime, @Param("id") Long id);

    /**
     * 查询学生已选课程列表
     *
     * @param studentId 学生ID
     * @return 已选课程列表
     */
    public List<StudentSelection> selectMySchedule(@Param("studentId") String studentId);

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
}

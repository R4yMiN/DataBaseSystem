package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StuCourse;

/**
 * 课程Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface StuCourseMapper 
{
    /**
     * 查询课程
     * 
     * @param courseId 课程主键
     * @return 课程
     */
    public StuCourse selectStuCourseByCourseId(String courseId);

    /**
     * 查询课程列表
     * 
     * @param stuCourse 课程
     * @return 课程集合
     */
    public List<StuCourse> selectStuCourseList(StuCourse stuCourse);

    /**
     * 新增课程
     * 
     * @param stuCourse 课程
     * @return 结果
     */
    public int insertStuCourse(StuCourse stuCourse);

    /**
     * 修改课程
     * 
     * @param stuCourse 课程
     * @return 结果
     */
    public int updateStuCourse(StuCourse stuCourse);

    /**
     * 删除课程
     * 
     * @param courseId 课程主键
     * @return 结果
     */
    public int deleteStuCourseByCourseId(String courseId);

    /**
     * 批量删除课程
     * 
     * @param courseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuCourseByCourseIds(String[] courseIds);
}

package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuCourseMapper;
import com.ruoyi.school.domain.StuCourse;
import com.ruoyi.school.service.IStuCourseService;

/**
 * 课程Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class StuCourseServiceImpl implements IStuCourseService 
{
    @Autowired
    private StuCourseMapper stuCourseMapper;

    /**
     * 查询课程
     * 
     * @param courseId 课程主键
     * @return 课程
     */
    @Override
    public StuCourse selectStuCourseByCourseId(String courseId)
    {
        return stuCourseMapper.selectStuCourseByCourseId(courseId);
    }

    /**
     * 查询课程列表
     * 
     * @param stuCourse 课程
     * @return 课程
     */
    @Override
    public List<StuCourse> selectStuCourseList(StuCourse stuCourse)
    {
        return stuCourseMapper.selectStuCourseList(stuCourse);
    }

    /**
     * 新增课程
     * 
     * @param stuCourse 课程
     * @return 结果
     */
    @Override
    public int insertStuCourse(StuCourse stuCourse)
    {
        return stuCourseMapper.insertStuCourse(stuCourse);
    }

    /**
     * 修改课程
     * 
     * @param stuCourse 课程
     * @return 结果
     */
    @Override
    public int updateStuCourse(StuCourse stuCourse)
    {
        return stuCourseMapper.updateStuCourse(stuCourse);
    }

    /**
     * 批量删除课程
     * 
     * @param courseIds 需要删除的课程主键
     * @return 结果
     */
    @Override
    public int deleteStuCourseByCourseIds(String[] courseIds)
    {
        return stuCourseMapper.deleteStuCourseByCourseIds(courseIds);
    }

    /**
     * 删除课程信息
     * 
     * @param courseId 课程主键
     * @return 结果
     */
    @Override
    public int deleteStuCourseByCourseId(String courseId)
    {
        return stuCourseMapper.deleteStuCourseByCourseId(courseId);
    }
}

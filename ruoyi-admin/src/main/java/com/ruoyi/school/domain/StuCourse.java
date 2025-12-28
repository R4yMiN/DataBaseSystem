package com.ruoyi.school.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程对象 stu_course
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class StuCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课号 */
    private String courseId;

    /** 课名 */
    @Excel(name = "课名")
    private String courseName;

    /** 学分 */
    @Excel(name = "学分")
    private Long credit;

    /** 学时 */
    @Excel(name = "学时")
    private Long creditHours;

    /** 院系号 */
    @Excel(name = "院系号")
    private String deptId;

    public void setCourseId(String courseId) 
    {
        this.courseId = courseId;
    }

    public String getCourseId() 
    {
        return courseId;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setCredit(Long credit) 
    {
        this.credit = credit;
    }

    public Long getCredit() 
    {
        return credit;
    }

    public void setCreditHours(Long creditHours) 
    {
        this.creditHours = creditHours;
    }

    public Long getCreditHours() 
    {
        return creditHours;
    }

    public void setDeptId(String deptId) 
    {
        this.deptId = deptId;
    }

    public String getDeptId() 
    {
        return deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("credit", getCredit())
            .append("creditHours", getCreditHours())
            .append("deptId", getDeptId())
            .toString();
    }
}

package com.ruoyi.school.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * VIEW对象 vw_student_schedule
 * 
 * @author ruoyi
 * @date 2026-01-03
 */
public class VwStudentSchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 学生学号 */
    @Excel(name = "学生学号")
    private String studentId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 课程ID/课号 */
    @Excel(name = "课程ID/课号")
    private String courseId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 教师工号 */
    @Excel(name = "教师工号")
    private String teacherId;

    /** 教师姓名 */
    @Excel(name = "教师姓名")
    private String teacherName;

    /** 学期 */
    @Excel(name = "学期")
    private String semester;

    /** 上课时间 */
    @Excel(name = "上课时间")
    private String classTime;

    /** 上课地点 */
    @Excel(name = "上课地点")
    private String classPlace;

    /** 选课状态（如：选课中/已退课） */
    @Excel(name = "选课状态")
    private String status;

    /** 是否被当前学生选中（0-未选 1-已选） */
    @Excel(name = "是否被当前学生选中", readConverterExp = "0=-未选,1=-已选")
    private Long isSelected;

    /** 学分 */
    @Excel(name = "学分")
    private Long credit;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

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

    public void setTeacherId(String teacherId) 
    {
        this.teacherId = teacherId;
    }

    public String getTeacherId() 
    {
        return teacherId;
    }

    public void setTeacherName(String teacherName) 
    {
        this.teacherName = teacherName;
    }

    public String getTeacherName() 
    {
        return teacherName;
    }

    public void setSemester(String semester) 
    {
        this.semester = semester;
    }

    public String getSemester() 
    {
        return semester;
    }

    public void setClassTime(String classTime) 
    {
        this.classTime = classTime;
    }

    public String getClassTime() 
    {
        return classTime;
    }

    public void setClassPlace(String classPlace) 
    {
        this.classPlace = classPlace;
    }

    public String getClassPlace() 
    {
        return classPlace;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setIsSelected(Long isSelected) 
    {
        this.isSelected = isSelected;
    }

    public Long getIsSelected() 
    {
        return isSelected;
    }

    public void setCredit(Long credit) 
    {
        this.credit = credit;
    }

    public Long getCredit() 
    {
        return credit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studentId", getStudentId())
            .append("studentName", getStudentName())
            .append("courseId", getCourseId())
            .append("courseName", getCourseName())
            .append("teacherId", getTeacherId())
            .append("teacherName", getTeacherName())
            .append("semester", getSemester())
            .append("classTime", getClassTime())
            .append("classPlace", getClassPlace())
            .append("status", getStatus())
            .append("isSelected", getIsSelected())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

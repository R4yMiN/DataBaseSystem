package com.ruoyi.school.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生自主选课对象 student_selection
 * 
 * @author admin
 * @date 2026-01-02
 */
public class StudentSelection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
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

    /** 课程总容量 */
    @Excel(name = "课程总容量")
    private Long capacity;

    /** 已选人数 */
    @Excel(name = "已选人数")
    private Long selectedNum;

    /** 剩余容量 */
    @Excel(name = "剩余容量")
    private Long remainCapacity;

    /** 选课状态（1-已选 0-已退课） */
    @Excel(name = "选课状态", readConverterExp = "1=-已选,0=-已退课")
    private Long status;

    /** 是否被当前学生选中（0-未选 1-已选） */
    @Excel(name = "是否被当前学生选中", readConverterExp = "0=-未选,1=-已选")
    private Long isSelected;

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

    public void setCapacity(Long capacity) 
    {
        this.capacity = capacity;
    }

    public Long getCapacity() 
    {
        return capacity;
    }

    public void setSelectedNum(Long selectedNum) 
    {
        this.selectedNum = selectedNum;
    }

    public Long getSelectedNum() 
    {
        return selectedNum;
    }

    public void setRemainCapacity(Long remainCapacity) 
    {
        this.remainCapacity = remainCapacity;
    }

    public Long getRemainCapacity() 
    {
        return remainCapacity;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
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
            .append("capacity", getCapacity())
            .append("selectedNum", getSelectedNum())
            .append("remainCapacity", getRemainCapacity())
            .append("status", getStatus())
            .append("isSelected", getIsSelected())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

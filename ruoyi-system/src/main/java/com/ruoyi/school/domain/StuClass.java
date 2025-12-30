package com.ruoyi.school.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 开课对象 stu_class
 * 
 * @author ruoyi
 * @date 2025-12-30
 */
public class StuClass extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 开课标识 */
    @Excel(name = "开课标识")
    private Long classId;

    /** 学期 */
    @Excel(name = "学期")
    private String semester;

    /** 课号 */
    @Excel(name = "课号")
    private String courseId;

    /** 工号 */
    @Excel(name = "工号")
    private String staffId;

    /** 上课时间 */
    @Excel(name = "上课时间")
    private String classTime;

    /** 课程容量 */
    @Excel(name = "课程容量")
    private Long capacity;

    /** 已选人数 */
    @Excel(name = "已选人数")
    private Long selectedNum;

    public void setClassId(Long classId) 
    {
        this.classId = classId;
    }

    public Long getClassId() 
    {
        return classId;
    }

    public void setSemester(String semester) 
    {
        this.semester = semester;
    }

    public String getSemester() 
    {
        return semester;
    }

    public void setCourseId(String courseId) 
    {
        this.courseId = courseId;
    }

    public String getCourseId() 
    {
        return courseId;
    }

    public void setStaffId(String staffId) 
    {
        this.staffId = staffId;
    }

    public String getStaffId() 
    {
        return staffId;
    }

    public void setClassTime(String classTime) 
    {
        this.classTime = classTime;
    }

    public String getClassTime() 
    {
        return classTime;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("classId", getClassId())
            .append("semester", getSemester())
            .append("courseId", getCourseId())
            .append("staffId", getStaffId())
            .append("classTime", getClassTime())
            .append("capacity", getCapacity())
            .append("selectedNum", getSelectedNum())
            .toString();
    }
}

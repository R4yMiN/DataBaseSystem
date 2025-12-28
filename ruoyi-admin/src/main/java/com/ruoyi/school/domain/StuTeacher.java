package com.ruoyi.school.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教师对象 stu_teacher
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class StuTeacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工号 */
    private String staffId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 性别（0男 1女 ） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String sex;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfBirth;

    /** 职称 */
    @Excel(name = "职称")
    private String professionalRanks;

    /** 基本工资 */
    @Excel(name = "基本工资")
    private BigDecimal salary;

    /** 院系号 */
    @Excel(name = "院系号")
    private String deptId;

    public void setStaffId(String staffId) 
    {
        this.staffId = staffId;
    }

    public String getStaffId() 
    {
        return staffId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setDateOfBirth(Date dateOfBirth) 
    {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfBirth() 
    {
        return dateOfBirth;
    }

    public void setProfessionalRanks(String professionalRanks) 
    {
        this.professionalRanks = professionalRanks;
    }

    public String getProfessionalRanks() 
    {
        return professionalRanks;
    }

    public void setSalary(BigDecimal salary) 
    {
        this.salary = salary;
    }

    public BigDecimal getSalary() 
    {
        return salary;
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
            .append("staffId", getStaffId())
            .append("name", getName())
            .append("sex", getSex())
            .append("dateOfBirth", getDateOfBirth())
            .append("professionalRanks", getProfessionalRanks())
            .append("salary", getSalary())
            .append("deptId", getDeptId())
            .toString();
    }
}

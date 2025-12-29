package com.ruoyi.school.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class StuTeacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String staffId; // 工号
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfBirth;
    @Excel(name = "职称")
    private String professionalRanks;
    @Excel(name = "基本工资")
    private BigDecimal salary;
    @Excel(name = "院系号")
    private String deptId;

    /** 关联系统用户ID (这是新加的) */
    private Long userId;

    public void setUserId(Long userId) { this.userId = userId; }
    public Long getUserId() { return userId; }

    public void setStaffId(String staffId) { this.staffId = staffId; }
    public String getStaffId() { return staffId; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setSex(String sex) { this.sex = sex; }
    public String getSex() { return sex; }

    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Date getDateOfBirth() { return dateOfBirth; }

    public void setProfessionalRanks(String professionalRanks) { this.professionalRanks = professionalRanks; }
    public String getProfessionalRanks() { return professionalRanks; }

    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public BigDecimal getSalary() { return salary; }

    public void setDeptId(String deptId) { this.deptId = deptId; }
    public String getDeptId() { return deptId; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("staffId", getStaffId())
                .append("name", getName())
                .append("userId", getUserId())
                .toString();
    }
}
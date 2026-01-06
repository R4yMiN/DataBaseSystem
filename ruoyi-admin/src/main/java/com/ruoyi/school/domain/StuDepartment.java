package com.ruoyi.school.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 院系对象 stu_department
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class StuDepartment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 院系号 */
    private String deptId;

    /** 名称 */
    @Excel(name = "名称")
    private String deptName;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phoneCode;

    public void setDeptId(String deptId) 
    {
        this.deptId = deptId;
    }

    public String getDeptId() 
    {
        return deptId;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setPhoneCode(String phoneCode) 
    {
        this.phoneCode = phoneCode;
    }

    public String getPhoneCode() 
    {
        return phoneCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("address", getAddress())
            .append("phoneCode", getPhoneCode())
            .toString();
    }
}

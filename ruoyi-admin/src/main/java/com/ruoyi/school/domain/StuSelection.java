package com.ruoyi.school.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 选课成绩对象 stu_selection
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class StuSelection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 选课记录ID */
    private Long selectionId;

    /** 学号 */
    @Excel(name = "学号")
    private String studentId;

    /** 开课ID */
    @Excel(name = "开课ID")
    private Long classId;

    /** 平时成绩 */
    @Excel(name = "平时成绩")
    private BigDecimal normalScore;

    /** 考试成绩 */
    @Excel(name = "考试成绩")
    private BigDecimal testScore;

    /** 总评成绩 */
    @Excel(name = "总评成绩")
    private BigDecimal totalScore;

    public void setSelectionId(Long selectionId) 
    {
        this.selectionId = selectionId;
    }

    public Long getSelectionId() 
    {
        return selectionId;
    }

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setClassId(Long classId) 
    {
        this.classId = classId;
    }

    public Long getClassId() 
    {
        return classId;
    }

    public void setNormalScore(BigDecimal normalScore) 
    {
        this.normalScore = normalScore;
    }

    public BigDecimal getNormalScore() 
    {
        return normalScore;
    }

    public void setTestScore(BigDecimal testScore) 
    {
        this.testScore = testScore;
    }

    public BigDecimal getTestScore() 
    {
        return testScore;
    }

    public void setTotalScore(BigDecimal totalScore) 
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getTotalScore() 
    {
        return totalScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("selectionId", getSelectionId())
            .append("studentId", getStudentId())
            .append("classId", getClassId())
            .append("normalScore", getNormalScore())
            .append("testScore", getTestScore())
            .append("totalScore", getTotalScore())
            .toString();
    }
}

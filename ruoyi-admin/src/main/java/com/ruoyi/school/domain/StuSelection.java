package com.ruoyi.school.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class StuSelection extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long selectionId;
    private String studentId;
    private Long classId;
    private BigDecimal normalScore;
    private BigDecimal testScore;
    private BigDecimal totalScore;

    // --- 下面是手动添加的扩展字段，用于页面显示，不要删 ---
    private String courseName;
    private String teacherName;
    private String classTime;
    private String classroomId;
    private Long capacity;
    private Long selectedNum;
    private Integer isSelected; // 0-未选 1-已选

    public void setSelectionId(Long selectionId) { this.selectionId = selectionId; }
    public Long getSelectionId() { return selectionId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentId() { return studentId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Long getClassId() { return classId; }
    public void setNormalScore(BigDecimal normalScore) { this.normalScore = normalScore; }
    public BigDecimal getNormalScore() { return normalScore; }
    public void setTestScore(BigDecimal testScore) { this.testScore = testScore; }
    public BigDecimal getTestScore() { return testScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public BigDecimal getTotalScore() { return totalScore; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getClassTime() { return classTime; }
    public void setClassTime(String classTime) { this.classTime = classTime; }
    public String getClassroomId() { return classroomId; }
    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
    public Long getCapacity() { return capacity; }
    public void setCapacity(Long capacity) { this.capacity = capacity; }
    public Long getSelectedNum() { return selectedNum; }
    public void setSelectedNum(Long selectedNum) { this.selectedNum = selectedNum; }
    public Integer getIsSelected() { return isSelected; }
    public void setIsSelected(Integer isSelected) { this.isSelected = isSelected; }
}
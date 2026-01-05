package com.ruoyi.school.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class StuClass extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long classId;
    private String semester;
    private String courseId;
    private String classSection;
    private String staffId;
    private Integer dayOfWeek;
    private Integer lessonStart;
    private Integer lessonEnd;

    // 【关键修复】补全 MyBatis 报错缺失的这三个字段
    private Integer weekStart;
    private Integer weekEnd;
    private Integer weekType;

    private String classroomId;
    private Long capacity;
    private Long selectedNum;
    private String status;

    /** 是否为主课时 (1是, 0追加) */
    private Integer isPrimary;

    // 扩展显示字段
    private String courseName;
    private String staffName;

    // --- Getter 和 Setter (必须全部包含) ---
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getClassSection() { return classSection; }
    public void setClassSection(String classSection) { this.classSection = classSection; }
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getLessonStart() { return lessonStart; }
    public void setLessonStart(Integer lessonStart) { this.lessonStart = lessonStart; }
    public Integer getLessonEnd() { return lessonEnd; }
    public void setLessonEnd(Integer lessonEnd) { this.lessonEnd = lessonEnd; }
    public Integer getWeekStart() { return weekStart; }
    public void setWeekStart(Integer weekStart) { this.weekStart = weekStart; }
    public Integer getWeekEnd() { return weekEnd; }
    public void setWeekEnd(Integer weekEnd) { this.weekEnd = weekEnd; }
    public Integer getWeekType() { return weekType; }
    public void setWeekType(Integer weekType) { this.weekType = weekType; }
    public String getClassroomId() { return classroomId; }
    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
    public Long getCapacity() { return capacity; }
    public void setCapacity(Long capacity) { this.capacity = capacity; }
    public Long getSelectedNum() { return selectedNum; }
    public void setSelectedNum(Long selectedNum) { this.selectedNum = selectedNum; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Integer isPrimary) { this.isPrimary = isPrimary; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
}
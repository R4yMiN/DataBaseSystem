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

    /** 课程名*/
    @Excel(name = "课程名称")
    private String courseName;

    /** 工号 */
    @Excel(name = "工号")
    private String staffId;

    /** 班级号 */
    @Excel(name = "班级号")
    private String classSection;

    /**教师姓名*/
    @Excel(name = "老师姓名")
    private String staffName;

    /** 星期几 (1-7) */
    @Excel(name = "星期几")
    private Integer dayOfWeek;

    /** 起始节次 (1-12) */
    @Excel(name = "起始节次")
    private Integer lessonStart;

    /** 结束节次 (1-12) */
    @Excel(name = "结束节次")
    private Integer lessonEnd;

    /** 起始周 (如1) */
    @Excel(name = "起始周")
    private Integer weekStart;

    /** 结束周 (如16) */
    @Excel(name = "结束周")
    private Integer weekEnd;

    /** 周类型 (0每周, 1单周, 2双周) */
    @Excel(name = "周类型")
    private Integer weekType;

    /** 教室标识 */
    @Excel(name = "教室标识")
    private String classroomId;

    /** 上课时间 (保留此字段用于显示原本的描述) */
    @Excel(name = "上课时间")
    private String classTime;

    /** 课程容量 */
    @Excel(name = "课程容量")
    private Long capacity;

    /** 已选人数 */
    @Excel(name = "已选人数")
    private Long selectedNum;

    // --- Getter 和 Setter 方法 ---

    public void setClassId(Long classId) { this.classId = classId; }
    public Long getClassId() { return classId; }

    public void setSemester(String semester) { this.semester = semester; }
    public String getSemester() { return semester; }

    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getCourseId() { return courseId; }

    public void setStaffId(String staffId) { this.staffId = staffId; }
    public String getStaffId() { return staffId; }

    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getDayOfWeek() { return dayOfWeek; }

    public void setLessonStart(Integer lessonStart) { this.lessonStart = lessonStart; }
    public Integer getLessonStart() { return lessonStart; }

    public void setLessonEnd(Integer lessonEnd) { this.lessonEnd = lessonEnd; }
    public Integer getLessonEnd() { return lessonEnd; }

    public void setWeekStart(Integer weekStart) { this.weekStart = weekStart; }
    public Integer getWeekStart() { return weekStart; }

    public void setWeekEnd(Integer weekEnd) { this.weekEnd = weekEnd; }
    public Integer getWeekEnd() { return weekEnd; }

    public void setWeekType(Integer weekType) { this.weekType = weekType; }
    public Integer getWeekType() { return weekType; }

    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }
    public String getClassroomId() { return classroomId; }

    public void setClassTime(String classTime) { this.classTime = classTime; }
    public String getClassTime() { return classTime; }

    public void setCapacity(Long capacity) { this.capacity = capacity; }
    public Long getCapacity() { return capacity; }

    public void setSelectedNum(Long selectedNum) { this.selectedNum = selectedNum; }
    public Long getSelectedNum() { return selectedNum; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("classId", getClassId())
                .append("semester", getSemester())
                .append("courseId", getCourseId())
                .append("staffId", getStaffId())
                .append("dayOfWeek", getDayOfWeek())
                .append("lessonStart", getLessonStart())
                .append("lessonEnd", getLessonEnd())
                .append("weekStart", getWeekStart())
                .append("weekEnd", getWeekEnd())
                .append("weekType", getWeekType())
                .append("classroomId", getClassroomId())
                .append("classTime", getClassTime())
                .append("capacity", getCapacity())
                .append("selectedNum", getSelectedNum())
                .append("staffName", getStaffName())
                .toString();
    }

    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getCourseName() { return courseName; }

    public void setClassSection(String classSection) { this.classSection = classSection; }
    public String getClassSection() { return classSection; }

    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getStaffName() { return staffName; }


}
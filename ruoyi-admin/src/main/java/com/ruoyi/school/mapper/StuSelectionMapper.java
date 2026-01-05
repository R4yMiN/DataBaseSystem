package com.ruoyi.school.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.school.domain.StuSelection;

/**
 * 选课成绩Mapper接口
 *
 * @author ruoyi
 * @date 2025-12-24
 */
public interface StuSelectionMapper
{
    /** 查询选课成绩 */
    public StuSelection selectStuSelectionBySelectionId(Long selectionId);

    /** 查询选课成绩列表 */
    public List<StuSelection> selectStuSelectionList(StuSelection stuSelection);

    /**
     * 我的已选课程（顶部表格专用）：只从 stu_selection 关联查询
     */
    public List<StuSelection> selectMySchedule(@Param("studentId") String studentId);

    /** 新增选课成绩 */
    public int insertStuSelection(StuSelection stuSelection);

    /** 修改选课成绩 */
    public int updateStuSelection(StuSelection stuSelection);

    /** 删除选课成绩 */
    public int deleteStuSelectionBySelectionId(Long selectionId);

    /** 批量删除选课成绩 */
    public int deleteStuSelectionBySelectionIds(Long[] selectionIds);

    /**
     * 校验：检查学生是否已经选过这门课
     */
    public int checkAlreadySelected(@Param("studentId") String studentId, @Param("classId") Long classId);

    /**
     * 校验：检查学生个人课表时间冲突
     * 原理：拿新课的时间去和该学生已选的所有课进行比对
     */
    public int checkStudentTimeConflict(@Param("studentId") String studentId,
                                        @Param("dayOfWeek") Integer dayOfWeek,
                                        @Param("lessonStart") Integer lessonStart,
                                        @Param("lessonEnd") Integer lessonEnd,
                                        @Param("weekStart") Integer weekStart,
                                        @Param("weekEnd") Integer weekEnd,
                                        @Param("weekType") Integer weekType);

    /**
     * 根据课程ID查询所有选课学生（成绩录入使用）
     */
    public List<StuSelection> selectStudentsByClassId(@Param("classId") Long classId);


}
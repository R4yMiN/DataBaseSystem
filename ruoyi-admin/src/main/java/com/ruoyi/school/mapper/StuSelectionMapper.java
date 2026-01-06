package com.ruoyi.school.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.school.domain.StuSelection;

public interface StuSelectionMapper
{
    public StuSelection selectStuSelectionBySelectionId(Long selectionId);

    public List<StuSelection> selectStuSelectionList(StuSelection stuSelection);

    public List<StuSelection> selectMySchedule(@Param("studentId") String studentId);

    public int insertStuSelection(StuSelection stuSelection);

    public int updateStuSelection(StuSelection stuSelection);

    public int deleteStuSelectionBySelectionId(Long selectionId);

    public int deleteStuSelectionBySelectionIds(Long[] selectionIds);

    public int deleteStuSelectionByStudentAndClassIds(@Param("studentId") String studentId, @Param("classIds") Long[] classIds);

    public int checkAlreadySelected(@Param("studentId") String studentId, @Param("classId") Long classId);

    public List<StuSelection> selectStudentsByClassId(@Param("classId") Long classId);

    /**
     * 查询所有超额的选课记录ID (用于随机踢人)
     */
    public List<Long> selectOverCapacitySelectionIds();

    /**
     * 查询学生自己的成绩
     */
    public List<StuSelection> selectMyGrades(StuSelection stuSelection);
}
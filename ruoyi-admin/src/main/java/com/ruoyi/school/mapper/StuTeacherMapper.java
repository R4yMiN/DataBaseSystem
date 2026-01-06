package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StuTeacher;

/**
 * 教师Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface StuTeacherMapper 
{
    /**
     * 查询教师
     * 
     * @param staffId 教师主键
     * @return 教师
     */
    public StuTeacher selectStuTeacherByStaffId(String staffId);

    /**
     * 查询教师列表
     * 
     * @param stuTeacher 教师
     * @return 教师集合
     */
    public List<StuTeacher> selectStuTeacherList(StuTeacher stuTeacher);

    /**
     * 新增教师
     * 
     * @param stuTeacher 教师
     * @return 结果
     */
    public int insertStuTeacher(StuTeacher stuTeacher);

    /**
     * 修改教师
     * 
     * @param stuTeacher 教师
     * @return 结果
     */
    public int updateStuTeacher(StuTeacher stuTeacher);

    /**
     * 删除教师
     * 
     * @param staffId 教师主键
     * @return 结果
     */
    public int deleteStuTeacherByStaffId(String staffId);

    /**
     * 批量删除教师
     * 
     * @param staffIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuTeacherByStaffIds(String[] staffIds);
}

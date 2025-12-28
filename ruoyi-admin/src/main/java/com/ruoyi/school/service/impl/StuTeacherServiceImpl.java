package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuTeacherMapper;
import com.ruoyi.school.domain.StuTeacher;
import com.ruoyi.school.service.IStuTeacherService;

/**
 * 教师Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class StuTeacherServiceImpl implements IStuTeacherService 
{
    @Autowired
    private StuTeacherMapper stuTeacherMapper;

    /**
     * 查询教师
     * 
     * @param staffId 教师主键
     * @return 教师
     */
    @Override
    public StuTeacher selectStuTeacherByStaffId(String staffId)
    {
        return stuTeacherMapper.selectStuTeacherByStaffId(staffId);
    }

    /**
     * 查询教师列表
     * 
     * @param stuTeacher 教师
     * @return 教师
     */
    @Override
    public List<StuTeacher> selectStuTeacherList(StuTeacher stuTeacher)
    {
        return stuTeacherMapper.selectStuTeacherList(stuTeacher);
    }

    /**
     * 新增教师
     * 
     * @param stuTeacher 教师
     * @return 结果
     */
    @Override
    public int insertStuTeacher(StuTeacher stuTeacher)
    {
        return stuTeacherMapper.insertStuTeacher(stuTeacher);
    }

    /**
     * 修改教师
     * 
     * @param stuTeacher 教师
     * @return 结果
     */
    @Override
    public int updateStuTeacher(StuTeacher stuTeacher)
    {
        return stuTeacherMapper.updateStuTeacher(stuTeacher);
    }

    /**
     * 批量删除教师
     * 
     * @param staffIds 需要删除的教师主键
     * @return 结果
     */
    @Override
    public int deleteStuTeacherByStaffIds(String[] staffIds)
    {
        return stuTeacherMapper.deleteStuTeacherByStaffIds(staffIds);
    }

    /**
     * 删除教师信息
     * 
     * @param staffId 教师主键
     * @return 结果
     */
    @Override
    public int deleteStuTeacherByStaffId(String staffId)
    {
        return stuTeacherMapper.deleteStuTeacherByStaffId(staffId);
    }
}

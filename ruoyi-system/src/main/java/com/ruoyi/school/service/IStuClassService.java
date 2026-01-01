package com.ruoyi.school.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.school.domain.StuClass;

/**
 * 开课Service接口
 */
public interface IStuClassService
{
    public StuClass selectStuClassByClassId(Long classId);

    public List<StuClass> selectStuClassList(StuClass stuClass);

    public int insertStuClass(StuClass stuClass);

    public int updateStuClass(StuClass stuClass);

    public int deleteStuClassByClassIds(Long[] classIds);

    public int deleteStuClassByClassId(Long classId);

    /** 随机抽签剔除超员学生 */
    public void executeRandomKick();

    /** 获取正式课程库列表，供老师点选 */
    public List<Map<String, Object>> selectAllCourseOptions();
}
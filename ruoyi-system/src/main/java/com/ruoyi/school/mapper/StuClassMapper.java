package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StuClass;

/**
 * 开课Mapper接口
 */
public interface StuClassMapper
{
    // ... 原有方法保持不变 ...
    public StuClass selectStuClassByClassId(Long classId);
    public List<StuClass> selectStuClassList(StuClass stuClass);
    public int insertStuClass(StuClass stuClass);
    public int updateStuClass(StuClass stuClass);
    public int deleteStuClassByClassId(Long classId);
    public int deleteStuClassByClassIds(Long[] classIds);
    public int executeRandomKick();

    /**
     * 检查排课冲突数量
     * @param stuClass 排课信息
     * @return 冲突数
     */
    public int checkSchedulingConflict(StuClass stuClass);

    /** 增加已选人数 (修改 stu_class 表) */
    public int plusSelectedNum(Long classId);

    /** 减少已选人数 (修改 stu_class 表) */
    public int minusSelectedNum(Long classId);
}
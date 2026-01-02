package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.VwStudentSchedule;

/**
 * VIEWMapper接口
 * 
 * @author ruoyi
 * @date 2026-01-03
 */
public interface VwStudentScheduleMapper 
{
    /**
     * 查询VIEW
     * 
     * @param id VIEW主键
     * @return VIEW
     */
    public VwStudentSchedule selectVwStudentScheduleById(Long id);

    /**
     * 查询VIEW列表
     * 
     * @param vwStudentSchedule VIEW
     * @return VIEW集合
     */
    public List<VwStudentSchedule> selectVwStudentScheduleList(VwStudentSchedule vwStudentSchedule);

    /**
     * 新增VIEW
     * 
     * @param vwStudentSchedule VIEW
     * @return 结果
     */
    public int insertVwStudentSchedule(VwStudentSchedule vwStudentSchedule);

    /**
     * 修改VIEW
     * 
     * @param vwStudentSchedule VIEW
     * @return 结果
     */
    public int updateVwStudentSchedule(VwStudentSchedule vwStudentSchedule);

    /**
     * 删除VIEW
     * 
     * @param id VIEW主键
     * @return 结果
     */
    public int deleteVwStudentScheduleById(Long id);

    /**
     * 批量删除VIEW
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVwStudentScheduleByIds(Long[] ids);
}

package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.VwStudentScheduleMapper;
import com.ruoyi.school.domain.VwStudentSchedule;
import com.ruoyi.school.service.IVwStudentScheduleService;

/**
 * VIEWService业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-03
 */
@Service
public class VwStudentScheduleServiceImpl implements IVwStudentScheduleService 
{
    @Autowired
    private VwStudentScheduleMapper vwStudentScheduleMapper;

    /**
     * 查询VIEW
     * 
     * @param id VIEW主键
     * @return VIEW
     */
    @Override
    public VwStudentSchedule selectVwStudentScheduleById(Long id)
    {
        return vwStudentScheduleMapper.selectVwStudentScheduleById(id);
    }

    /**
     * 查询VIEW列表
     * 
     * @param vwStudentSchedule VIEW
     * @return VIEW
     */
    @Override
    public List<VwStudentSchedule> selectVwStudentScheduleList(VwStudentSchedule vwStudentSchedule)
    {
        return vwStudentScheduleMapper.selectVwStudentScheduleList(vwStudentSchedule);
    }

    /**
     * 新增VIEW
     * 
     * @param vwStudentSchedule VIEW
     * @return 结果
     */
    @Override
    public int insertVwStudentSchedule(VwStudentSchedule vwStudentSchedule)
    {
        vwStudentSchedule.setCreateTime(DateUtils.getNowDate());
        return vwStudentScheduleMapper.insertVwStudentSchedule(vwStudentSchedule);
    }

    /**
     * 修改VIEW
     * 
     * @param vwStudentSchedule VIEW
     * @return 结果
     */
    @Override
    public int updateVwStudentSchedule(VwStudentSchedule vwStudentSchedule)
    {
        vwStudentSchedule.setUpdateTime(DateUtils.getNowDate());
        return vwStudentScheduleMapper.updateVwStudentSchedule(vwStudentSchedule);
    }

    /**
     * 批量删除VIEW
     * 
     * @param ids 需要删除的VIEW主键
     * @return 结果
     */
    @Override
    public int deleteVwStudentScheduleByIds(Long[] ids)
    {
        return vwStudentScheduleMapper.deleteVwStudentScheduleByIds(ids);
    }

    /**
     * 删除VIEW信息
     * 
     * @param id VIEW主键
     * @return 结果
     */
    @Override
    public int deleteVwStudentScheduleById(Long id)
    {
        return vwStudentScheduleMapper.deleteVwStudentScheduleById(id);
    }
}

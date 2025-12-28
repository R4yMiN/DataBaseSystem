package com.ruoyi.school.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuDepartmentMapper;
import com.ruoyi.school.domain.StuDepartment;
import com.ruoyi.school.service.IStuDepartmentService;

/**
 * 院系Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class StuDepartmentServiceImpl implements IStuDepartmentService 
{
    @Autowired
    private StuDepartmentMapper stuDepartmentMapper;

    /**
     * 查询院系
     * 
     * @param deptId 院系主键
     * @return 院系
     */
    @Override
    public StuDepartment selectStuDepartmentByDeptId(String deptId)
    {
        return stuDepartmentMapper.selectStuDepartmentByDeptId(deptId);
    }

    /**
     * 查询院系列表
     * 
     * @param stuDepartment 院系
     * @return 院系
     */
    @Override
    public List<StuDepartment> selectStuDepartmentList(StuDepartment stuDepartment)
    {
        return stuDepartmentMapper.selectStuDepartmentList(stuDepartment);
    }

    /**
     * 新增院系
     * 
     * @param stuDepartment 院系
     * @return 结果
     */
    @Override
    public int insertStuDepartment(StuDepartment stuDepartment)
    {
        return stuDepartmentMapper.insertStuDepartment(stuDepartment);
    }

    /**
     * 修改院系
     * 
     * @param stuDepartment 院系
     * @return 结果
     */
    @Override
    public int updateStuDepartment(StuDepartment stuDepartment)
    {
        return stuDepartmentMapper.updateStuDepartment(stuDepartment);
    }

    /**
     * 批量删除院系
     * 
     * @param deptIds 需要删除的院系主键
     * @return 结果
     */
    @Override
    public int deleteStuDepartmentByDeptIds(String[] deptIds)
    {
        return stuDepartmentMapper.deleteStuDepartmentByDeptIds(deptIds);
    }

    /**
     * 删除院系信息
     * 
     * @param deptId 院系主键
     * @return 结果
     */
    @Override
    public int deleteStuDepartmentByDeptId(String deptId)
    {
        return stuDepartmentMapper.deleteStuDepartmentByDeptId(deptId);
    }
}

package com.ruoyi.school.mapper;

import java.util.List;
import com.ruoyi.school.domain.StuDepartment;

/**
 * 院系Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface StuDepartmentMapper 
{
    /**
     * 查询院系
     * 
     * @param deptId 院系主键
     * @return 院系
     */
    public StuDepartment selectStuDepartmentByDeptId(String deptId);

    /**
     * 查询院系列表
     * 
     * @param stuDepartment 院系
     * @return 院系集合
     */
    public List<StuDepartment> selectStuDepartmentList(StuDepartment stuDepartment);

    /**
     * 新增院系
     * 
     * @param stuDepartment 院系
     * @return 结果
     */
    public int insertStuDepartment(StuDepartment stuDepartment);

    /**
     * 修改院系
     * 
     * @param stuDepartment 院系
     * @return 结果
     */
    public int updateStuDepartment(StuDepartment stuDepartment);

    /**
     * 删除院系
     * 
     * @param deptId 院系主键
     * @return 结果
     */
    public int deleteStuDepartmentByDeptId(String deptId);

    /**
     * 批量删除院系
     * 
     * @param deptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuDepartmentByDeptIds(String[] deptIds);
}

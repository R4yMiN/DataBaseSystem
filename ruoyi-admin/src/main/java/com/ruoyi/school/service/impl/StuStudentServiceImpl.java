package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser; // 必须导入
import com.ruoyi.common.utils.SecurityUtils;      // 必须导入
import com.ruoyi.system.service.ISysUserService;  // 必须导入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuStudentMapper;
import com.ruoyi.school.domain.StuStudent;
import com.ruoyi.school.service.IStuStudentService;
import org.springframework.transaction.annotation.Transactional; // 必须导入

/**
 * 学生Service业务层处理
 *
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class StuStudentServiceImpl implements IStuStudentService
{
    @Autowired
    private StuStudentMapper stuStudentMapper;

    @Autowired
    private ISysUserService userService; // 注入系统用户服务

    /**
     * 查询学生
     */
    @Override
    public StuStudent selectStuStudentByStudentId(String studentId)
    {
        return stuStudentMapper.selectStuStudentByStudentId(studentId);
    }

    /**
     * 查询学生列表
     */
    @Override
    public List<StuStudent> selectStuStudentList(StuStudent stuStudent)
    {
        return stuStudentMapper.selectStuStudentList(stuStudent);
    }

    /**
     * 【核心改动】新增学生：同步创建系统登录账号
     */
    @Override
    @Transactional // 开启事务，保证两边数据一致
    public int insertStuStudent(StuStudent stuStudent)
    {
        // 1. 创建对应的系统用户对象
        SysUser user = new SysUser();
        user.setUserName(stuStudent.getStudentId()); // 学号作为登录账号名
        user.setNickName(stuStudent.getName());      // 姓名作为昵称
        user.setPassword(SecurityUtils.encryptPassword("123456")); // 初始密码123456
        user.setDeptId(100L); // 默认归属顶级部门

        // 2. 分配角色：这里填你查到的【学生】角色的编号
        // 注意：请去“角色管理”确认学生的角色编号，这里假设是 4
        Long[] roleIds = {102L};
        user.setRoleIds(roleIds);

        // 3. 执行若依系统用户新增
        userService.insertUser(user);

        // 4. 获取生成的 userId，回填给学生对象
        stuStudent.setUserId(user.getUserId());

        // 5. 保存学生档案到学生表
        return stuStudentMapper.insertStuStudent(stuStudent);
    }

    /**
     * 修改学生
     */
    @Override
    public int updateStuStudent(StuStudent stuStudent)
    {
        return stuStudentMapper.updateStuStudent(stuStudent);
    }

    /**
     * 【核心改动】批量删除学生：同步注销系统账号
     */
    @Override
    @Transactional
    public int deleteStuStudentByStudentIds(String[] studentIds)
    {
        for (String id : studentIds) {
            StuStudent student = stuStudentMapper.selectStuStudentByStudentId(id);
            if (student != null && student.getUserId() != null) {
                // 删除对应的系统账号
                userService.deleteUserById(student.getUserId());
            }
        }
        return stuStudentMapper.deleteStuStudentByStudentIds(studentIds);
    }

    /**
     * 【核心改动】删除单个学生：同步注销系统账号
     */
    @Override
    @Transactional
    public int deleteStuStudentByStudentId(String studentId)
    {
        StuStudent student = stuStudentMapper.selectStuStudentByStudentId(studentId);
        if (student != null && student.getUserId() != null) {
            userService.deleteUserById(student.getUserId());
        }
        return stuStudentMapper.deleteStuStudentByStudentId(studentId);
    }
}
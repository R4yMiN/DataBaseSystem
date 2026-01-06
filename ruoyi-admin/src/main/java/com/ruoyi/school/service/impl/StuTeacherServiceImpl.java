package com.ruoyi.school.service.impl;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser;      // 关键导入
import com.ruoyi.common.utils.SecurityUtils;           // 关键导入
import com.ruoyi.system.service.ISysUserService;      // 关键导入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.school.mapper.StuTeacherMapper;
import com.ruoyi.school.domain.StuTeacher;
import com.ruoyi.school.service.IStuTeacherService;
import org.springframework.transaction.annotation.Transactional; // 关键导入

/**
 * 教师Service业务层处理
 */
@Service
public class StuTeacherServiceImpl implements IStuTeacherService
{
    @Autowired
    private StuTeacherMapper stuTeacherMapper;

    @Autowired
    private ISysUserService userService; // 注入系统用户服务

    @Override
    public StuTeacher selectStuTeacherByStaffId(String staffId)
    {
        return stuTeacherMapper.selectStuTeacherByStaffId(staffId);
    }

    @Override
    public List<StuTeacher> selectStuTeacherList(StuTeacher stuTeacher)
    {
        return stuTeacherMapper.selectStuTeacherList(stuTeacher);
    }

    /**
     * 【核心修改】新增教师：同步创建登录账号
     */
    @Override
    @Transactional // 开启事务控制
    public int insertStuTeacher(StuTeacher stuTeacher)
    {
        System.err.println(">>>>>> 调试启动：正在尝试自动创建账号。工号：" + stuTeacher.getStaffId());
        // 1. 创建系统登录账号对象
        SysUser user = new SysUser();
        user.setUserName(stuTeacher.getStaffId()); // 登录名 = 教师工号
        user.setNickName(stuTeacher.getName());    // 昵称 = 教师姓名
        user.setPassword(SecurityUtils.encryptPassword("123456")); // 默认密码
        user.setDeptId(100L); // 默认部门

        // 2. 分配角色：假设教师角色ID是3（请去后台角色管理确认一下）
        Long[] roleIds = {101L};
        user.setRoleIds(roleIds);

        // 3. 调用若依自带的创建用户接口
        userService.insertUser(user);

        // 4. 获取刚生成的 userId，回填给教师档案
        stuTeacher.setUserId(user.getUserId());

        // 5. 保存教师档案
        return stuTeacherMapper.insertStuTeacher(stuTeacher);
    }

    /**
     * 【核心修改】删除教师：同步删除账号
     */
    @Override
    @Transactional
    public int deleteStuTeacherByStaffIds(String[] staffIds)
    {
        for (String id : staffIds) {
            StuTeacher teacher = stuTeacherMapper.selectStuTeacherByStaffId(id);
            if (teacher != null && teacher.getUserId() != null) {
                userService.deleteUserById(teacher.getUserId());
            }
        }
        return stuTeacherMapper.deleteStuTeacherByStaffIds(staffIds);
    }

    @Override
    public int updateStuTeacher(StuTeacher stuTeacher)
    {
        return stuTeacherMapper.updateStuTeacher(stuTeacher);
    }

    @Override
    @Transactional
    public int deleteStuTeacherByStaffId(String staffId)
    {
        StuTeacher teacher = stuTeacherMapper.selectStuTeacherByStaffId(staffId);
        if (teacher != null && teacher.getUserId() != null) {
            userService.deleteUserById(teacher.getUserId());
        }
        return stuTeacherMapper.deleteStuTeacherByStaffId(staffId);
    }
}
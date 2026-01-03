package com.ruoyi.school.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;

@RestController
@RequestMapping("/school/class")
public class StuClassController extends BaseController
{
    @Autowired
    private IStuClassService stuClassService;

    /**
     * 教务/老师列表：修正权限判断报错问题
     */
    @PreAuthorize("@ss.hasPermi('school:class:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuClass stuClass)
    {
        // 1. 获取当前登录用户信息
        com.ruoyi.common.core.domain.model.LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserId();
        String username = loginUser.getUsername();

        // 2. 权限判断逻辑修正
        // 判断是否是管理员
        boolean isAdmin = SecurityUtils.isAdmin(userId);

        // 判断是否拥有 "dean" (教务) 角色
        boolean isDean = loginUser.getUser().getRoles().stream()
                .anyMatch(role -> "dean".equals(role.getRoleKey()));

        // 3. 如果既不是管理员也不是教务，则只能查看自己名下的课程
        if (!isAdmin && !isDean) {
            stuClass.setStaffId(username);
        }

        startPage();
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);
        return getDataTable(list);
    }

    /**
     * 获取详细信息
     */
    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable("classId") Long classId)
    {
        return success(stuClassService.selectStuClassByClassId(classId));
    }

    /**
     * 新增开课（自动绑定教师）
     */
    @PreAuthorize("@ss.hasPermi('school:class:add')")
    @Log(title = "开课", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuClass stuClass)
    {
        // 老师申请时，如果没填工号，默认设为当前用户
        if (stuClass.getStaffId() == null || stuClass.getStaffId().trim().isEmpty()) {
            stuClass.setStaffId(SecurityUtils.getUsername());
        }
        return toAjax(stuClassService.insertStuClass(stuClass));
    }

    /**
     * 修改排课
     */
    @PreAuthorize("@ss.hasPermi('school:class:edit')")
    @Log(title = "开课", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuClass stuClass)
    {
        return toAjax(stuClassService.updateStuClass(stuClass));
    }

    /**
     * 删除排课
     */
    @PreAuthorize("@ss.hasPermi('school:class:remove')")
    @Log(title = "开课", businessType = BusinessType.DELETE)
    @DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds)
    {
        return toAjax(stuClassService.deleteStuClassByClassIds(classIds));
    }

    /**
     * 获取正式课程库列表
     */
    @GetMapping("/courseList")
    public AjaxResult getCourseList()
    {
        return AjaxResult.success(stuClassService.selectAllCourseOptions());
    }

    /**
     * 随机抽签剔除逻辑
     */
    @PreAuthorize("@ss.hasPermi('school:class:kick')")
    @PostMapping("/randomKick")
    public AjaxResult randomKick()
    {
        stuClassService.executeRandomKick();
        return success("抽签处理完成");
    }
}
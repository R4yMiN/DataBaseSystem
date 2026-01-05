package com.ruoyi.school.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;

@RestController
@RequestMapping("/school/class")
public class StuClassController extends BaseController {
    @Autowired
    private IStuClassService stuClassService;

    @PreAuthorize("@ss.hasPermi('school:class:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuClass stuClass) {
        // 1. 判定是否为管理员或教务
        boolean isAdmin = SecurityUtils.isAdmin(SecurityUtils.getUserId())
                || SecurityUtils.getLoginUser().getUser().getRoles().stream().anyMatch(r -> "dean".equals(r.getRoleKey()));

        // 2. 获取前端发来的 viewType (list 或 visual)
        String viewType = (String) stuClass.getParams().get("viewType");

        // 3. 如果前端没有传isPrimary，则根据角色和viewType来决定
        if (stuClass.getIsPrimary() == null) {
            if (!isAdmin) {
                // 老师视角：绑定工号
                stuClass.setStaffId(SecurityUtils.getUsername());

                // 逻辑：如果是列表模式，隐藏追加课(isPrimary=1)；
                // 如果是可视化模式(visual)，显示自己名下所有的课，包括追加的
                if ("list".equals(viewType)) {
                    stuClass.setIsPrimary(1);
                }
            }
        } else {
            // 前端明确传了isPrimary，保留前端的值
            // 但如果不是管理员，仍然要绑定工号
            if (!isAdmin) {
                stuClass.setStaffId(SecurityUtils.getUsername());
            }
        }

        startPage();
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);
        return getDataTable(list);
    }

    @PostMapping
    public AjaxResult add(@RequestBody StuClass stuClass) {
        return toAjax(stuClassService.insertStuClass(stuClass));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody StuClass stuClass) {
        return toAjax(stuClassService.updateStuClass(stuClass));
    }

    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable("classId") Long classId) {
        return success(stuClassService.selectStuClassByClassId(classId));
    }

    @DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds) {
        return toAjax(stuClassService.deleteStuClassByClassIds(classIds));
    }

    @GetMapping("/courseList")
    public AjaxResult getCourseList() {
        return success(stuClassService.selectAllCourseOptions());
    }

    @GetMapping("/classroomList")
    public AjaxResult getClassroomList() {
        return success(stuClassService.selectAllClassroomOptions());
    }
}
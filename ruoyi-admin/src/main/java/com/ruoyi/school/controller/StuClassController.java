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
        boolean isAdmin = SecurityUtils.isAdmin(SecurityUtils.getUserId())
                || SecurityUtils.getLoginUser().getUser().getRoles().stream().anyMatch(r -> "dean".equals(r.getRoleKey()));

        String viewType = (String) stuClass.getParams().get("viewType");

        if (stuClass.getIsPrimary() == null) {
            if (!isAdmin) {
                stuClass.setStaffId(SecurityUtils.getUsername());
                if ("list".equals(viewType)) {
                    stuClass.setIsPrimary(1);
                }
            }
        } else {
            if (!isAdmin) {
                stuClass.setStaffId(SecurityUtils.getUsername());
            }
        }

        startPage();
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasRole('admin')")
    @PostMapping("/randomKick")
    public AjaxResult randomKick() {
        stuClassService.executeRandomKick();
        return success("随机抽签完成，已剔除超员学生并释放名额");
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
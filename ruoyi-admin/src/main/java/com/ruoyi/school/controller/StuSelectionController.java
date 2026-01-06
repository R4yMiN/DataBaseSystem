package com.ruoyi.school.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.school.domain.StuSelection;
import com.ruoyi.school.service.IStuSelectionService;

@RestController
@RequestMapping("/school/selection")
public class StuSelectionController extends BaseController {

    @Autowired
    private IStuSelectionService stuSelectionService;

    /**
     * 选课大厅列表（解决学生看不到数据的问题）
     */
    @GetMapping("/list")
    public TableDataInfo list(StuSelection stuSelection) {
        // 获取当前登录人
        String username = SecurityUtils.getUsername();
        boolean isAdmin = SecurityUtils.isAdmin(SecurityUtils.getUserId());

        // 无论是不是管理员，都要传入当前用户名给 XML 里的 LEFT JOIN 使用
        // 这样学生才能在“全表”里看到哪些是自己已经选了的（蓝/红按钮切换）
        stuSelection.setStudentId(username);

        startPage();
        List<StuSelection> list = stuSelectionService.selectStuSelectionList(stuSelection);
        return getDataTable(list);
    }

    /**
     * 我的已选课程（顶部小表专用）
     */
    @GetMapping("/mySchedule")
    public TableDataInfo mySchedule() {
        String studentId = SecurityUtils.getUsername();
        List<StuSelection> list = stuSelectionService.selectMySchedule(studentId);
        return getDataTable(list);
    }

    /**
     * 选课操作 (POST)
     */
    @PostMapping
    public AjaxResult add(@RequestBody StuSelection stuSelection) {
        stuSelection.setStudentId(SecurityUtils.getUsername());
        return toAjax(stuSelectionService.insertStuSelection(stuSelection));
    }

    /**
     * 退选操作 (DELETE)
     */
    @DeleteMapping("/{selectionIds}")
    public AjaxResult remove(@PathVariable Long[] selectionIds) {
        return toAjax(stuSelectionService.deleteStuSelectionBySelectionIds(selectionIds));
    }

    /**
     * 查询选了某门课的所有学生（供成绩录入使用）
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,dean,teacher')")
    @GetMapping("/students")
    public TableDataInfo getStudentsByClass(@RequestParam("classId") Long classId) {
        List<StuSelection> list = stuSelectionService.selectStudentsByClassId(classId);
        return getDataTable(list);
    }

    /**
     * 修改选课记录（成绩录入）
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,dean,teacher')")
    @PutMapping
    public AjaxResult edit(@RequestBody StuSelection stuSelection) {
        return toAjax(stuSelectionService.updateStuSelection(stuSelection));
    }
}
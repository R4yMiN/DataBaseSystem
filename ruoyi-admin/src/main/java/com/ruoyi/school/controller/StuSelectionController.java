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
        StuSelection query = new StuSelection();
        query.setStudentId(SecurityUtils.getUsername());
        // 增加标记，告诉 XML 只查已选中的
        query.getParams().put("onlyMy", "1");

        List<StuSelection> list = stuSelectionService.selectStuSelectionList(query);
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
}
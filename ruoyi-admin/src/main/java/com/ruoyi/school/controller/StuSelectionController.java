package com.ruoyi.school.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.school.domain.StuSelection;
import com.ruoyi.school.service.IStuSelectionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 选课成绩Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/school/selection")
public class StuSelectionController extends BaseController
{
    @Autowired
    private IStuSelectionService stuSelectionService;

    /**
     * 查询选课成绩列表
     */
    @PreAuthorize("@ss.hasPermi('school:selection:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuSelection stuSelection)
    {
        startPage();
        List<StuSelection> list = stuSelectionService.selectStuSelectionList(stuSelection);
        return getDataTable(list);
    }

    /**
     * 导出选课成绩列表
     */
    @PreAuthorize("@ss.hasPermi('school:selection:export')")
    @Log(title = "选课成绩", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuSelection stuSelection)
    {
        List<StuSelection> list = stuSelectionService.selectStuSelectionList(stuSelection);
        ExcelUtil<StuSelection> util = new ExcelUtil<StuSelection>(StuSelection.class);
        util.exportExcel(response, list, "选课成绩数据");
    }

    /**
     * 获取选课成绩详细信息
     */
    @PreAuthorize("@ss.hasPermi('school:selection:query')")
    @GetMapping(value = "/{selectionId}")
    public AjaxResult getInfo(@PathVariable("selectionId") Long selectionId)
    {
        return success(stuSelectionService.selectStuSelectionBySelectionId(selectionId));
    }

    /**
     * 新增选课成绩
     */
    @PreAuthorize("@ss.hasPermi('school:selection:add')")
    @Log(title = "选课成绩", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuSelection stuSelection)
    {
        return toAjax(stuSelectionService.insertStuSelection(stuSelection));
    }

    /**
     * 修改选课成绩
     */
    @PreAuthorize("@ss.hasPermi('school:selection:edit')")
    @Log(title = "选课成绩", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuSelection stuSelection)
    {
        return toAjax(stuSelectionService.updateStuSelection(stuSelection));
    }

    /**
     * 删除选课成绩
     */
    @PreAuthorize("@ss.hasPermi('school:selection:remove')")
    @Log(title = "选课成绩", businessType = BusinessType.DELETE)
	@DeleteMapping("/{selectionIds}")
    public AjaxResult remove(@PathVariable Long[] selectionIds)
    {
        return toAjax(stuSelectionService.deleteStuSelectionBySelectionIds(selectionIds));
    }
}

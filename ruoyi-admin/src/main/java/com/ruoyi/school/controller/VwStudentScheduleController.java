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
import com.ruoyi.school.domain.VwStudentSchedule;
import com.ruoyi.school.service.IVwStudentScheduleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * VIEWController
 * 
 * @author ruoyi
 * @date 2026-01-03
 */
@RestController
@RequestMapping("/system/schedule")
public class VwStudentScheduleController extends BaseController
{
    @Autowired
    private IVwStudentScheduleService vwStudentScheduleService;

    /**
     * 查询VIEW列表
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:list')")
    @GetMapping("/list")
    public TableDataInfo list(VwStudentSchedule vwStudentSchedule)
    {
        startPage();
        List<VwStudentSchedule> list = vwStudentScheduleService.selectVwStudentScheduleList(vwStudentSchedule);
        return getDataTable(list);
    }

    /**
     * 导出VIEW列表
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:export')")
    @Log(title = "VIEW", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VwStudentSchedule vwStudentSchedule)
    {
        List<VwStudentSchedule> list = vwStudentScheduleService.selectVwStudentScheduleList(vwStudentSchedule);
        ExcelUtil<VwStudentSchedule> util = new ExcelUtil<VwStudentSchedule>(VwStudentSchedule.class);
        util.exportExcel(response, list, "VIEW数据");
    }

    /**
     * 获取VIEW详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(vwStudentScheduleService.selectVwStudentScheduleById(id));
    }

    /**
     * 新增VIEW
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:add')")
    @Log(title = "VIEW", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VwStudentSchedule vwStudentSchedule)
    {
        return toAjax(vwStudentScheduleService.insertVwStudentSchedule(vwStudentSchedule));
    }

    /**
     * 修改VIEW
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:edit')")
    @Log(title = "VIEW", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VwStudentSchedule vwStudentSchedule)
    {
        return toAjax(vwStudentScheduleService.updateVwStudentSchedule(vwStudentSchedule));
    }

    /**
     * 删除VIEW
     */
    @PreAuthorize("@ss.hasPermi('system:schedule:remove')")
    @Log(title = "VIEW", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(vwStudentScheduleService.deleteVwStudentScheduleByIds(ids));
    }
}

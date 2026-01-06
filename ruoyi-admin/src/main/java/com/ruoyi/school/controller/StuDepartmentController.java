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
import com.ruoyi.school.domain.StuDepartment;
import com.ruoyi.school.service.IStuDepartmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 院系Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/school/department")
public class StuDepartmentController extends BaseController
{
    @Autowired
    private IStuDepartmentService stuDepartmentService;

    /**
     * 查询院系列表
     */
    @PreAuthorize("@ss.hasPermi('school:department:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuDepartment stuDepartment)
    {
        startPage();
        List<StuDepartment> list = stuDepartmentService.selectStuDepartmentList(stuDepartment);
        return getDataTable(list);
    }

    /**
     * 导出院系列表
     */
    @PreAuthorize("@ss.hasPermi('school:department:export')")
    @Log(title = "院系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuDepartment stuDepartment)
    {
        List<StuDepartment> list = stuDepartmentService.selectStuDepartmentList(stuDepartment);
        ExcelUtil<StuDepartment> util = new ExcelUtil<StuDepartment>(StuDepartment.class);
        util.exportExcel(response, list, "院系数据");
    }

    /**
     * 获取院系详细信息
     */
    @PreAuthorize("@ss.hasPermi('school:department:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable("deptId") String deptId)
    {
        return success(stuDepartmentService.selectStuDepartmentByDeptId(deptId));
    }

    /**
     * 新增院系
     */
    @PreAuthorize("@ss.hasPermi('school:department:add')")
    @Log(title = "院系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuDepartment stuDepartment)
    {
        return toAjax(stuDepartmentService.insertStuDepartment(stuDepartment));
    }

    /**
     * 修改院系
     */
    @PreAuthorize("@ss.hasPermi('school:department:edit')")
    @Log(title = "院系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuDepartment stuDepartment)
    {
        return toAjax(stuDepartmentService.updateStuDepartment(stuDepartment));
    }

    /**
     * 删除院系
     */
    @PreAuthorize("@ss.hasPermi('school:department:remove')")
    @Log(title = "院系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deptIds}")
    public AjaxResult remove(@PathVariable String[] deptIds)
    {
        return toAjax(stuDepartmentService.deleteStuDepartmentByDeptIds(deptIds));
    }
}

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
import com.ruoyi.school.domain.StuTeacher;
import com.ruoyi.school.service.IStuTeacherService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教师Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/school/teacher")
public class StuTeacherController extends BaseController
{
    @Autowired
    private IStuTeacherService stuTeacherService;

    /**
     * 查询教师列表
     */
    @PreAuthorize("@ss.hasPermi('school:teacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuTeacher stuTeacher)
    {
        startPage();
        List<StuTeacher> list = stuTeacherService.selectStuTeacherList(stuTeacher);
        return getDataTable(list);
    }

    /**
     * 导出教师列表
     */
    @PreAuthorize("@ss.hasPermi('school:teacher:export')")
    @Log(title = "教师", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuTeacher stuTeacher)
    {
        List<StuTeacher> list = stuTeacherService.selectStuTeacherList(stuTeacher);
        ExcelUtil<StuTeacher> util = new ExcelUtil<StuTeacher>(StuTeacher.class);
        util.exportExcel(response, list, "教师数据");
    }

    /**
     * 获取教师详细信息
     */
    @PreAuthorize("@ss.hasPermi('school:teacher:query')")
    @GetMapping(value = "/{staffId}")
    public AjaxResult getInfo(@PathVariable("staffId") String staffId)
    {
        return success(stuTeacherService.selectStuTeacherByStaffId(staffId));
    }

    /**
     * 新增教师
     */
    @PreAuthorize("@ss.hasPermi('school:teacher:add')")
    @Log(title = "教师", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuTeacher stuTeacher)
    {
        return toAjax(stuTeacherService.insertStuTeacher(stuTeacher));
    }

    /**
     * 修改教师
     */
    @PreAuthorize("@ss.hasPermi('school:teacher:edit')")
    @Log(title = "教师", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuTeacher stuTeacher)
    {
        return toAjax(stuTeacherService.updateStuTeacher(stuTeacher));
    }

    /**
     * 删除教师
     */
    @PreAuthorize("@ss.hasPermi('school:teacher:remove')")
    @Log(title = "教师", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable String[] staffIds)
    {
        return toAjax(stuTeacherService.deleteStuTeacherByStaffIds(staffIds));
    }
}

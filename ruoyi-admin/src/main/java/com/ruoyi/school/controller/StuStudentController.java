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
import com.ruoyi.school.domain.StuStudent;
import com.ruoyi.school.service.IStuStudentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学生Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/school/student")
public class StuStudentController extends BaseController
{
    @Autowired
    private IStuStudentService stuStudentService;

    /**
     * 查询学生列表
     */
    @PreAuthorize("@ss.hasPermi('school:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuStudent stuStudent)
    {
        startPage();
        List<StuStudent> list = stuStudentService.selectStuStudentList(stuStudent);
        return getDataTable(list);
    }

    /**
     * 导出学生列表
     */
    @PreAuthorize("@ss.hasPermi('school:student:export')")
    @Log(title = "学生", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuStudent stuStudent)
    {
        List<StuStudent> list = stuStudentService.selectStuStudentList(stuStudent);
        ExcelUtil<StuStudent> util = new ExcelUtil<StuStudent>(StuStudent.class);
        util.exportExcel(response, list, "学生数据");
    }

    /**
     * 获取学生详细信息
     */
    @PreAuthorize("@ss.hasPermi('school:student:query')")
    @GetMapping(value = "/{studentId}")
    public AjaxResult getInfo(@PathVariable("studentId") String studentId)
    {
        return success(stuStudentService.selectStuStudentByStudentId(studentId));
    }

    /**
     * 新增学生
     */
    @PreAuthorize("@ss.hasPermi('school:student:add')")
    @Log(title = "学生", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuStudent stuStudent)
    {
        return toAjax(stuStudentService.insertStuStudent(stuStudent));
    }

    /**
     * 修改学生
     */
    @PreAuthorize("@ss.hasPermi('school:student:edit')")
    @Log(title = "学生", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuStudent stuStudent)
    {
        return toAjax(stuStudentService.updateStuStudent(stuStudent));
    }

    /**
     * 删除学生
     */
    @PreAuthorize("@ss.hasPermi('school:student:remove')")
    @Log(title = "学生", businessType = BusinessType.DELETE)
	@DeleteMapping("/{studentIds}")
    public AjaxResult remove(@PathVariable String[] studentIds)
    {
        return toAjax(stuStudentService.deleteStuStudentByStudentIds(studentIds));
    }
}

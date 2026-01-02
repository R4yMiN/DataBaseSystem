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
import com.ruoyi.school.domain.StudentSelection;
import com.ruoyi.school.service.IStudentSelectionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学生自主选课Controller
 * 
 * @author admin
 * @date 2026-01-02
 */
@RestController
@RequestMapping("/system/selection")
public class StudentSelectionController extends BaseController
{
    @Autowired
    private IStudentSelectionService studentSelectionService;

    /**
     * 查询学生自主选课列表
     */
    @PreAuthorize("@ss.hasPermi('system:selection:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudentSelection studentSelection)
    {
        startPage();
        List<StudentSelection> list = studentSelectionService.selectStudentSelectionList(studentSelection);
        return getDataTable(list);
    }

    /**
     * 导出学生自主选课列表
     */
    @PreAuthorize("@ss.hasPermi('system:selection:export')")
    @Log(title = "学生自主选课", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudentSelection studentSelection)
    {
        List<StudentSelection> list = studentSelectionService.selectStudentSelectionList(studentSelection);
        ExcelUtil<StudentSelection> util = new ExcelUtil<StudentSelection>(StudentSelection.class);
        util.exportExcel(response, list, "学生自主选课数据");
    }

    /**
     * 获取学生自主选课详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:selection:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(studentSelectionService.selectStudentSelectionById(id));
    }

    /**
     * 新增学生自主选课
     */
    @PreAuthorize("@ss.hasPermi('system:selection:add')")
    @Log(title = "学生自主选课", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudentSelection studentSelection)
    {
        return toAjax(studentSelectionService.insertStudentSelection(studentSelection));
    }

    /**
     * 修改学生自主选课
     */
    @PreAuthorize("@ss.hasPermi('system:selection:edit')")
    @Log(title = "学生自主选课", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudentSelection studentSelection)
    {
        return toAjax(studentSelectionService.updateStudentSelection(studentSelection));
    }

    /**
     * 删除学生自主选课
     */
    @PreAuthorize("@ss.hasPermi('system:selection:remove')")
    @Log(title = "学生自主选课", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(studentSelectionService.deleteStudentSelectionByIds(ids));
    }

    /**
     * 学生选择课程
     */
    @PreAuthorize("@ss.hasPermi('system:selection:list')")
    @PostMapping("/select/{id}")
    public AjaxResult select(@PathVariable("id") Long id)
    {
        return toAjax(studentSelectionService.selectCourse(id));
    }

    /**
     * 学生退选课程
     */
    @PreAuthorize("@ss.hasPermi('system:selection:list')")
    @PostMapping("/unselect/{id}")
    public AjaxResult unselect(@PathVariable("id") Long id)
    {
        return toAjax(studentSelectionService.unselectCourse(id));
    }
}

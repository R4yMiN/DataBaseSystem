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
import com.ruoyi.school.domain.StuCourse;
import com.ruoyi.school.service.IStuCourseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 课程Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/school/course")
public class StuCourseController extends BaseController
{
    @Autowired
    private IStuCourseService stuCourseService;

    /**
     * 查询课程列表
     */
    @PreAuthorize("@ss.hasPermi('school:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuCourse stuCourse)
    {
        startPage();
        List<StuCourse> list = stuCourseService.selectStuCourseList(stuCourse);
        return getDataTable(list);
    }

    /**
     * 导出课程列表
     */
    @PreAuthorize("@ss.hasPermi('school:course:export')")
    @Log(title = "课程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuCourse stuCourse)
    {
        List<StuCourse> list = stuCourseService.selectStuCourseList(stuCourse);
        ExcelUtil<StuCourse> util = new ExcelUtil<StuCourse>(StuCourse.class);
        util.exportExcel(response, list, "课程数据");
    }

    /**
     * 获取课程详细信息
     */
    @PreAuthorize("@ss.hasPermi('school:course:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") String courseId)
    {
        return success(stuCourseService.selectStuCourseByCourseId(courseId));
    }

    /**
     * 新增课程
     */
    @PreAuthorize("@ss.hasPermi('school:course:add')")
    @Log(title = "课程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuCourse stuCourse)
    {
        return toAjax(stuCourseService.insertStuCourse(stuCourse));
    }

    /**
     * 修改课程
     */
    @PreAuthorize("@ss.hasPermi('school:course:edit')")
    @Log(title = "课程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuCourse stuCourse)
    {
        return toAjax(stuCourseService.updateStuCourse(stuCourse));
    }

    /**
     * 删除课程
     */
    @PreAuthorize("@ss.hasPermi('school:course:remove')")
    @Log(title = "课程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable String[] courseIds)
    {
        return toAjax(stuCourseService.deleteStuCourseByCourseIds(courseIds));
    }
}

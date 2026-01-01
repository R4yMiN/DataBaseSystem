package com.ruoyi.school.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.school.domain.StuClass;
import com.ruoyi.school.service.IStuClassService;

@RestController
@RequestMapping("/school/class")
public class StuClassController extends BaseController
{
    @Autowired
    private IStuClassService stuClassService;

    /** 管理端列表查询 */
    @PreAuthorize("@ss.hasPermi('school:class:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuClass stuClass) {
        startPage();
        return getDataTable(stuClassService.selectStuClassList(stuClass));
    }

    /** 学生端列表：脱敏 + 自动转换数字为时间文字 */
    @GetMapping("/studentList")
    public TableDataInfo studentList(StuClass stuClass) {
        startPage();
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);

        String[] days = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        List<StuClass> safeList = list.stream().peek(item -> {
            // 安全保护：如果数据库里这些数字是空的，给个默认值防止报错
            Integer day = item.getDayOfWeek() == null ? 0 : item.getDayOfWeek();
            Integer s = item.getLessonStart() == null ? 0 : item.getLessonStart();
            Integer e = item.getLessonEnd() == null ? 0 : item.getLessonEnd();
            Integer ws = item.getWeekStart() == null ? 0 : item.getWeekStart();
            Integer we = item.getWeekEnd() == null ? 0 : item.getWeekEnd();

            // 拼装时间描述给前端显示
            String timeDesc = String.format("%s 第%d-%d节 (%d-%d周)",
                    (day > 0 && day < 8) ? days[day] : "时间待定", s, e, ws, we);

            item.setStaffId(null); // 隐藏老师工号，保护隐私
            item.setClassTime(timeDesc); // 借用 classTime 字段传回拼好的文字
        }).collect(Collectors.toList());

        return getDataTable(safeList);
    }

    /** 获取详细信息（修复修改按钮 404 的关键！） */
    @PreAuthorize("@ss.hasPermi('school:class:query')")
    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable("classId") Long classId) {
        return success(stuClassService.selectStuClassByClassId(classId));
    }

    /** 新增开课 */
    @PreAuthorize("@ss.hasPermi('school:class:add')")
    @Log(title = "开课", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuClass stuClass) {
        return toAjax(stuClassService.insertStuClass(stuClass));
    }

    /** 修改开课 */
    @PreAuthorize("@ss.hasPermi('school:class:edit')")
    @Log(title = "开课", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuClass stuClass) {
        return toAjax(stuClassService.updateStuClass(stuClass));
    }

    /** 删除开课（修复删除按钮 404 的关键！） */
    @PreAuthorize("@ss.hasPermi('school:class:remove')")
    @Log(title = "开课", businessType = BusinessType.DELETE)
    @DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds) {
        return toAjax(stuClassService.deleteStuClassByClassIds(classIds));
    }

    /** 随机抽签 */
    @PreAuthorize("@ss.hasPermi('school:class:kick')") // 只有拥有此权限的用户能点
    @Log(title = "开课管理", businessType = BusinessType.UPDATE)
    @PostMapping("/randomKick")
    public AjaxResult randomKick() {
        stuClassService.executeRandomKick();
        return success("抽签清算成功");
    }
}
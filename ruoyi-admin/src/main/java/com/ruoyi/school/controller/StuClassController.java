package com.ruoyi.school.controller;

import java.util.*;
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

    /**
     * 1. 管理端列表 (教务老师看：看到的是每一条原始排课记录，方便修改具体时间)
     */
    @PreAuthorize("@ss.hasPermi('school:class:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuClass stuClass) {
        // 1. 获取当前登录用户信息
        String username = com.ruoyi.common.utils.SecurityUtils.getUsername();
        boolean isAdmin = com.ruoyi.common.utils.SecurityUtils.isAdmin(com.ruoyi.common.utils.SecurityUtils.getUserId());

        // 2. 权限逻辑：如果不是管理员，强行只能查自己的工号
        if (!isAdmin) {
            stuClass.setStaffId(username);
        }

        startPage();
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);

        // 统一去重：同一课程/班级/学期/时间段/教室的重复记录合并，避免管理员和老师端看到重复
        String[] days = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        Map<String, List<StuClass>> grouped = list.stream()
                .collect(Collectors.groupingBy(c -> String.join("-",
                        safe(c.getCourseId()),
                        safe(c.getClassSection()),
                        safe(c.getSemester()),
                        String.valueOf(c.getDayOfWeek()),
                        String.valueOf(c.getLessonStart()),
                        String.valueOf(c.getLessonEnd()),
                        safe(c.getClassroomId())
                )));

        List<StuClass> merged = new ArrayList<>();
        grouped.forEach((k, subList) -> {
            StuClass main = subList.get(0);
            String fullTimeDesc = subList.stream()
                    .map(t -> {
                        if (t.getDayOfWeek() == null || t.getLessonStart() == null || t.getLessonEnd() == null) {
                            return "待排课";
                        }
                        String day = t.getDayOfWeek() >= 1 && t.getDayOfWeek() <= 7 ? days[t.getDayOfWeek()] : "";
                        String room = t.getClassroomId() == null ? "" : (" [" + t.getClassroomId() + "]");
                        return String.format("%s第%d-%d节%s", t.getDayOfWeek() == null ? "" : day, t.getLessonStart(), t.getLessonEnd(), room);
                    })
                    .distinct()
                    .collect(Collectors.joining(" | "));
            main.setClassTime(fullTimeDesc);
            merged.add(main);
        });

        return getDataTable(merged);
    }

    /** 安全空值处理 */
    private String safe(String val) {
        return val == null ? "" : val;
    }

    /**
     * 2. 学生端列表 (核心：实现你说的 Group By 聚合逻辑)
     * 作用：把多节课合并为一行，并提取时间地点
     */
    @GetMapping("/studentList")
    public TableDataInfo studentList(StuClass stuClass) {
        // A. 查询所有原始排课数据
        List<StuClass> list = stuClassService.selectStuClassList(stuClass);

        // B. 转换工具：数字转文字
        String[] days = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        // C. 使用 Java 分组逻辑 (相当于 MySQL 的 GROUP BY course_id, staff_id)
        Map<String, List<StuClass>> grouped = list.stream()
                .collect(Collectors.groupingBy(c -> c.getCourseId() + "-" + c.getStaffId()));

        // D. 将分组后的数据重新组装成前端需要的格式
        List<StuClass> resultList = new ArrayList<>();

        grouped.forEach((key, subList) -> {
            // 拿每组的第一条数据作为基础（学期、容量等信息）
            StuClass main = subList.get(0);

            // 核心：把这组所有的 [时间+教室] 提取并拼接 (提取与放置的原理)
            String fullTimeDesc = subList.stream()
                    .map(t -> String.format("%s第%d-%d节[%s]",
                            days[t.getDayOfWeek()], t.getLessonStart(), t.getLessonEnd(), t.getClassroomId()))
                    .collect(Collectors.joining(" | "));

            // 将拼好的长字符串（如：周一1-2节[101] | 周三3-4节[202]）塞给 classTime 字段显示
            main.setClassTime(fullTimeDesc);
            main.setStaffId(null); // 脱敏，隐藏老师工号

            resultList.add(main);
        });

        return getDataTable(resultList);
    }

    /**
     * 3. 获取详细信息 (修改回显用)
     */
    @GetMapping(value = "/{classId}")
    public AjaxResult getInfo(@PathVariable("classId") Long classId) {
        return success(stuClassService.selectStuClassByClassId(classId));
    }

    /**
     * 新增开课（自动绑定教师工号）
     */
    @PreAuthorize("@ss.hasPermi('school:class:add')")
    @Log(title = "开课", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuClass stuClass)
    {
        // 关键点：如果工号为空（说明是老师在申请），则自动获取当前登录用户的用户名作为工号
        if (stuClass.getStaffId() == null || stuClass.getStaffId().equals("")) {
            stuClass.setStaffId(com.ruoyi.common.utils.SecurityUtils.getUsername());
        }

        return toAjax(stuClassService.insertStuClass(stuClass));
    }

    /**
     * 5. 修改排课信息
     */
    @PreAuthorize("@ss.hasPermi('school:class:edit')")
    @Log(title = "开课", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuClass stuClass) {
        return toAjax(stuClassService.updateStuClass(stuClass));
    }

    /**
     * 6. 删除排课
     */
    @PreAuthorize("@ss.hasPermi('school:class:remove')")
    @Log(title = "开课", businessType = BusinessType.DELETE)
    @DeleteMapping("/{classIds}")
    public AjaxResult remove(@PathVariable Long[] classIds) {
        return toAjax(stuClassService.deleteStuClassByClassIds(classIds));
    }

    /**
     * 7. 随机踢人 (教务专供)
     */
    @PreAuthorize("@ss.hasPermi('school:class:kick')")
    @PostMapping("/randomKick")
    public AjaxResult randomKick() {
        stuClassService.executeRandomKick();
        return success("随机抽签清算成功");
    }

    /**
     * 获取正式课程库列表，供老师点选
     */
    @GetMapping("/courseList")
    public AjaxResult getCourseList()
    {
        // 还是用你原来的 stuClassService，只是调用我们新加的方法
        return AjaxResult.success(stuClassService.selectAllCourseOptions());
    }
}
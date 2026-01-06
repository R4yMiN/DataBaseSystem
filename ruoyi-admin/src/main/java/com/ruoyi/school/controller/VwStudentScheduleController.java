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
import java.io.IOException;
import java.net.URLEncoder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.school.domain.StuSelection;
import com.ruoyi.school.domain.VwStudentSchedule;
import com.ruoyi.school.service.IStuSelectionService;
import com.ruoyi.school.service.IVwStudentScheduleService;
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

    @Autowired
    private IStuSelectionService stuSelectionService;

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
    public void export(HttpServletResponse response, VwStudentSchedule vwStudentSchedule) throws IOException
    {
        // 使用与前端课表相同的数据源，包含主课+追加时段
        String studentId = SecurityUtils.getLoginUser().getUser().getUserName();
        List<StuSelection> scheduleList = stuSelectionService.selectMySchedule(studentId);
        
        // 转换为 VwStudentSchedule 以复用现有 PDF 生成逻辑
        List<VwStudentSchedule> list = new ArrayList<>();
        for (StuSelection sel : scheduleList) {
            VwStudentSchedule vs = new VwStudentSchedule();
            vs.setStudentId(sel.getStudentId());
            vs.setCourseId(sel.getCourseId());
            vs.setCourseName(sel.getCourseName());
            vs.setTeacherName(sel.getTeacherName());
            vs.setSemester(sel.getSemester());
            vs.setClassTime(sel.getClassTime());
            vs.setClassPlace(sel.getClassroomId());
            vs.setCredit(sel.getCredit());
            list.add(vs);
        }
        
        // 计算总学分
        int totalCredits = 0;
        for (VwStudentSchedule item : list) {
            if (item.getCredit() != null) {
                totalCredits += item.getCredit();
            }
        }

        PDDocument doc = new PDDocument();
        FontPair fonts = loadFonts(doc);
        buildTimetablePdf(doc, list, fonts, totalCredits);
        buildPendingPdf(doc, list, fonts);

        String fileName = "student_schedule_" + DateUtils.dateTimeNow() + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        doc.save(response.getOutputStream());
        doc.close();
    }

    /** 将导出格式改成与前端课表视图一致的网格布局 (PDF) - 合并单元格版 */
    private void buildTimetablePdf(PDDocument doc, List<VwStudentSchedule> list, FontPair fonts, int totalCredits) throws IOException {
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        float margin = 30f;
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        
        // 顶部信息区域高度
        float headerHeight = 70f;
        float tableTop = pageHeight - margin - headerHeight;
        float leftX = margin;
        float rowHeight = 42f;
        float headerRowHeight = 30f;
        float firstColWidth = 50f;
        float tableWidth = pageWidth - 2 * margin;
        float dayColWidth = (tableWidth - firstColWidth) / 7f;

        // 获取学生信息
        String studentName = "";
        String studentId = "";
        String semester = "";
        if (!list.isEmpty()) {
            VwStudentSchedule first = list.get(0);
            studentName = safeText(first.getStudentName());
            studentId = safeText(first.getStudentId());
            semester = safeText(first.getSemester());
        }

        // 构建课程块数据 (合并连续节次)
        List<CourseBlock> blocks = buildCourseBlocks(list);

        // 定义课程卡片颜色 (柔和的颜色)
        float[][] cardColors = {
            {0.85f, 0.92f, 1.00f},   // 浅蓝
            {0.95f, 0.88f, 0.95f},   // 浅紫
            {0.88f, 0.96f, 0.88f},   // 浅绿
            {1.00f, 0.93f, 0.85f},   // 浅橙
            {0.98f, 0.90f, 0.90f},   // 浅红
            {0.90f, 0.96f, 0.96f},   // 浅青
            {0.98f, 0.98f, 0.88f},   // 浅黄
        };

        try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
            // ========== 顶部标题区域 ==========
            cs.beginText();
            cs.setFont(fonts.bold, 20);
            cs.newLineAtOffset(pageWidth / 2 - 60, pageHeight - margin - 28);
            cs.showText("学生课程表");
            cs.endText();

            // 学生信息行
            cs.beginText();
            cs.setFont(fonts.regular, 11);
            cs.newLineAtOffset(margin, pageHeight - margin - 52);
            cs.showText("姓名: " + studentName + "    学号: " + studentId + "    学期: " + semester + "    总学分: " + totalCredits);
            cs.endText();

            // 分隔线
            cs.setStrokingColor(0.7f, 0.7f, 0.7f);
            cs.setLineWidth(0.5f);
            cs.moveTo(margin, tableTop + 5);
            cs.lineTo(pageWidth - margin, tableTop + 5);
            cs.stroke();

            // ========== 表头背景 ==========
            cs.setNonStrokingColor(0.25f, 0.45f, 0.65f); // 深蓝色背景
            cs.addRect(leftX, tableTop - headerRowHeight, tableWidth, headerRowHeight);
            cs.fill();

            // ========== 绘制完整表格网格 ==========
            float tableBottom = tableTop - headerRowHeight - 12 * rowHeight;
            
            // 外边框
            cs.setStrokingColor(0.5f, 0.5f, 0.5f);
            cs.setLineWidth(1f);
            cs.addRect(leftX, tableBottom, tableWidth, tableTop - tableBottom);
            cs.stroke();

            // 画所有横线 (每节一行)
            cs.setLineWidth(0.3f);
            cs.setStrokingColor(0.75f, 0.75f, 0.75f);
            for (int r = 0; r <= 12; r++) {
                float y = tableTop - headerRowHeight - r * rowHeight;
                cs.moveTo(leftX, y);
                cs.lineTo(leftX + tableWidth, y);
                cs.stroke();
            }

            // 画所有竖线
            float x = leftX;
            cs.moveTo(x, tableTop);
            cs.lineTo(x, tableBottom);
            cs.stroke();
            
            x = leftX + firstColWidth;
            cs.setStrokingColor(0.6f, 0.6f, 0.6f);
            cs.setLineWidth(0.5f);
            cs.moveTo(x, tableTop);
            cs.lineTo(x, tableBottom);
            cs.stroke();
            
            cs.setStrokingColor(0.75f, 0.75f, 0.75f);
            cs.setLineWidth(0.3f);
            for (int c = 0; c < 7; c++) {
                x = leftX + firstColWidth + (c + 1) * dayColWidth;
                cs.moveTo(x, tableTop);
                cs.lineTo(x, tableBottom);
                cs.stroke();
            }

            // ========== 写表头文字 (白色) ==========
            cs.setNonStrokingColor(1f, 1f, 1f);
            
            cs.beginText();
            cs.setFont(fonts.bold, 10);
            float headerTextY = tableTop - headerRowHeight / 2 - 4;
            cs.newLineAtOffset(leftX + 12, headerTextY);
            cs.showText("节次");
            cs.endText();

            String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
            for (int i = 0; i < days.length; i++) {
                float colCenter = leftX + firstColWidth + dayColWidth * i + dayColWidth / 2 - 12;
                cs.beginText();
                cs.setFont(fonts.bold, 10);
                cs.newLineAtOffset(colCenter, headerTextY);
                cs.showText(days[i]);
                cs.endText();
            }

            // ========== 左侧节次列 ==========
            // 写节次文字
            cs.setNonStrokingColor(0.25f, 0.25f, 0.25f);
            for (int r = 0; r < 12; r++) {
                float cellTop = tableTop - headerRowHeight - r * rowHeight;
                float cellMidY = cellTop - rowHeight / 2 + 4;
                cs.beginText();
                cs.setFont(fonts.regular, 9);
                cs.newLineAtOffset(leftX + 8, cellMidY);
                cs.showText("第" + (r + 1) + "节");
                cs.endText();
            }

            // ========== 绘制课程卡片 (合并单元格显示) ==========
            int colorIdx = 0;
            for (CourseBlock block : blocks) {
                float cardLeft = leftX + firstColWidth + (block.day - 1) * dayColWidth + 3;
                float cardTop = tableTop - headerRowHeight - (block.startPeriod - 1) * rowHeight - 3;
                float cardHeight = (block.endPeriod - block.startPeriod + 1) * rowHeight - 6;
                float cardWidth = dayColWidth - 6;

                // 卡片背景 (圆角矩形)
                float[] color = cardColors[colorIdx % cardColors.length];
                cs.setNonStrokingColor(color[0], color[1], color[2]);
                drawRoundedRect(cs, cardLeft, cardTop - cardHeight, cardWidth, cardHeight, 4f);
                cs.fill();

                // 卡片边框
                cs.setStrokingColor(color[0] - 0.2f, color[1] - 0.2f, color[2] - 0.2f);
                cs.setLineWidth(0.8f);
                drawRoundedRect(cs, cardLeft, cardTop - cardHeight, cardWidth, cardHeight, 4f);
                cs.stroke();

                // 卡片文字
                float textX = cardLeft + 5;
                float textY = cardTop - 13;
                float lineHeight = 11f;

                // 课程名称 (加粗，深色)
                cs.setNonStrokingColor(0.1f, 0.1f, 0.1f);
                cs.beginText();
                cs.setFont(fonts.bold, 9);
                cs.newLineAtOffset(textX, textY);
                String courseName = block.courseName;
                if (courseName.length() > 9) {
                    courseName = courseName.substring(0, 8) + "..";
                }
                cs.showText(courseName);
                cs.endText();
                textY -= lineHeight;

                // 上课地点 (灰色)
                if (block.classPlace != null && !block.classPlace.isEmpty()) {
                    cs.setNonStrokingColor(0.35f, 0.35f, 0.35f);
                    cs.beginText();
                    cs.setFont(fonts.regular, 8);
                    cs.newLineAtOffset(textX, textY);
                    String place = block.classPlace;
                    if (place.length() > 9) {
                        place = place.substring(0, 8) + "..";
                    }
                    cs.showText(place);
                    cs.endText();
                    textY -= lineHeight;
                }

                // 教师姓名 (浅灰)
                if (block.teacherName != null && !block.teacherName.isEmpty()) {
                    cs.setNonStrokingColor(0.45f, 0.45f, 0.45f);
                    cs.beginText();
                    cs.setFont(fonts.regular, 8);
                    cs.newLineAtOffset(textX, textY);
                    cs.showText(block.teacherName);
                    cs.endText();
                    textY -= lineHeight;
                }

                // 节次范围 (最浅灰)
                cs.setNonStrokingColor(0.55f, 0.55f, 0.55f);
                cs.beginText();
                cs.setFont(fonts.regular, 7);
                cs.newLineAtOffset(textX, textY);
                cs.showText("第" + block.startPeriod + "-" + block.endPeriod + "节");
                cs.endText();

                colorIdx++;
            }

            // ========== 页脚 ==========
            cs.beginText();
            cs.setFont(fonts.regular, 8);
            cs.setNonStrokingColor(0.6f, 0.6f, 0.6f);
            cs.newLineAtOffset(pageWidth / 2 - 50, margin - 15);
            cs.showText("导出时间: " + DateUtils.getDate());
            cs.endText();
        }
    }

    /** 绘制圆角矩形路径 */
    private void drawRoundedRect(PDPageContentStream cs, float x, float y, float width, float height, float radius) throws IOException {
        cs.moveTo(x + radius, y);
        cs.lineTo(x + width - radius, y);
        cs.curveTo(x + width, y, x + width, y, x + width, y + radius);
        cs.lineTo(x + width, y + height - radius);
        cs.curveTo(x + width, y + height, x + width, y + height, x + width - radius, y + height);
        cs.lineTo(x + radius, y + height);
        cs.curveTo(x, y + height, x, y + height, x, y + height - radius);
        cs.lineTo(x, y + radius);
        cs.curveTo(x, y, x, y, x + radius, y);
        cs.closePath();
    }

    /** 构建课程块 (合并连续节次) */
    private List<CourseBlock> buildCourseBlocks(List<VwStudentSchedule> list) {
        List<CourseBlock> blocks = new ArrayList<>();
        Pattern pattern = Pattern.compile("周([一二三四五六日天]|\\d)[^\\d]*?(\\d+)(?:[-~至到—](\\d+))?");
        
        for (VwStudentSchedule item : list) {
            List<Slot> slots = parseSlots(item.getClassTime(), pattern);
            for (Slot s : slots) {
                if (s.day < 1 || s.day > 7 || s.start < 1 || s.end > 12) continue;
                
                CourseBlock block = new CourseBlock();
                block.day = s.day;
                block.startPeriod = s.start;
                block.endPeriod = Math.min(s.end, 12);
                block.courseName = item.getCourseName() != null ? item.getCourseName() : item.getCourseId();
                block.teacherName = item.getTeacherName();
                block.classPlace = item.getClassPlace();
                blocks.add(block);
            }
        }
        return blocks;
    }

    /** 课程块数据结构 */
    private static class CourseBlock {
        int day;           // 1-7 表示周一到周日
        int startPeriod;   // 开始节次
        int endPeriod;     // 结束节次
        String courseName;
        String teacherName;
        String classPlace;
    }

    /** 生成待排课列表 PDF */
    private void buildPendingPdf(PDDocument doc, List<VwStudentSchedule> list, FontPair fonts) throws IOException {
        List<VwStudentSchedule> pending = new ArrayList<>();
        Pattern pattern = Pattern.compile("周([一二三四五六日天]|\\d)[^\\d]*?(\\d+)(?:[-~至到—](\\d+))?");
        for (VwStudentSchedule item : list) {
            List<Slot> slots = parseSlots(item.getClassTime(), pattern);
            if (slots.isEmpty()) {
                pending.add(item);
            }
        }
        if (pending.isEmpty()) {
            return;
        }

        float margin = 40f;
        float rowHeight = 20f;
        float[] colWidths = {140f, 80f, 80f, 100f, 140f};
        String[] headers = {"课程名称", "教师", "学期", "上课地点", "上课时间"};

        int idx = 0;
        while (idx < pending.size()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            float y = page.getMediaBox().getHeight() - margin;

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.setStrokingColor(0f, 0f, 0f);

                // 表头
                cs.beginText();
                cs.setFont(fonts.bold, 12);
                cs.newLineAtOffset(margin, y);
                cs.showText("待排课");
                cs.endText();
                y -= 14;

                drawRow(cs, margin, y, rowHeight, colWidths, headers, true, fonts);
                y -= rowHeight;

                while (idx < pending.size() && y >= margin + rowHeight) {
                    VwStudentSchedule item = pending.get(idx);
                    String[] cells = {
                            safeText(item.getCourseName()),
                            safeText(item.getTeacherName()),
                            safeText(item.getSemester()),
                            safeText(item.getClassPlace()),
                            safeText(item.getClassTime())
                    };
                    drawRow(cs, margin, y, rowHeight, colWidths, cells, false, fonts);
                    y -= rowHeight;
                    idx++;
                }
            }
        }
    }

    private void drawRow(PDPageContentStream cs, float x, float y, float rowHeight, float[] widths, String[] values, boolean header, FontPair fonts) throws IOException {
        float curX = x;
        // 背景线框
        float rowWidth = 0;
        for (float w : widths) rowWidth += w;
        cs.addRect(x, y - rowHeight, rowWidth, rowHeight);
        cs.stroke();
        // 列分隔
        for (int i = 0; i < widths.length - 1; i++) {
            curX += widths[i];
            cs.moveTo(curX, y - rowHeight);
            cs.lineTo(curX, y);
            cs.stroke();
        }
        // 文本
        curX = x + 4;
        float textY = y - 14;
        cs.beginText();
        cs.setFont(header ? fonts.bold : fonts.regular, 10);
        cs.newLineAtOffset(curX, textY);
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                cs.newLineAtOffset(widths[i - 1], 0);
            }
            cs.showText(values[i] == null ? "" : values[i]);
        }
        cs.endText();
    }

    private List<Slot> parseSlots(String classTime, Pattern preparedPattern) {
        List<Slot> slots = new ArrayList<>();
        if (classTime == null || classTime.trim().isEmpty()) {
            return slots;
        }
        Pattern pattern = preparedPattern != null ? preparedPattern : Pattern.compile("周([一二三四五六日天]|\\d)[^\\d]*?(\\d+)(?:[-~至到—](\\d+))?");
        String[] parts = classTime.split("\\|");
        for (String part : parts) {
            String p = part.trim();
            if (p.isEmpty()) continue;
            Matcher m = pattern.matcher(p);
            if (m.find()) {
                int day = mapDay(m.group(1));
                int start = safeInt(m.group(2), 1);
                int end = safeInt(m.group(3), start);
                if (day >= 1 && day <= 7) {
                    slots.add(new Slot(day, start, end));
                }
            }
        }
        return slots;
    }

    private List<List<List<String>>> buildGrid(List<VwStudentSchedule> list) {
        List<List<List<String>>> grid = new ArrayList<>();
        for (int r = 0; r < 12; r++) {
            List<List<String>> row = new ArrayList<>();
            for (int c = 0; c < 7; c++) {
                row.add(new ArrayList<>());
            }
            grid.add(row);
        }
        Pattern pattern = Pattern.compile("周([一二三四五六日天]|\\d)[^\\d]*?(\\d+)(?:[-~至到—](\\d+))?");
        for (VwStudentSchedule item : list) {
            List<Slot> slots = parseSlots(item.getClassTime(), pattern);
            if (slots.isEmpty()) {
                continue;
            }
            for (Slot s : slots) {
                for (int p = s.start; p <= s.end && p <= 12; p++) {
                    int rowIdx = p - 1;
                    int colIdx = s.day - 1;
                    if (rowIdx < 0 || colIdx < 0 || colIdx >= 7) {
                        continue;
                    }
                    grid.get(rowIdx).get(colIdx).add(buildCellText(item, s));
                }
            }
        }
        return grid;
    }

    private int mapDay(String token) {
        switch (token) {
            case "一": return 1; case "二": return 2; case "三": return 3; case "四": return 4; case "五": return 5; case "六": return 6; case "日":
            case "天": return 7; default: return safeInt(token, 0);
        }
    }

    private int safeInt(String v, int def) {
        try { return v == null ? def : Integer.parseInt(v); } catch (NumberFormatException ex) { return def; }
    }

    private String safeText(String v) { return v == null ? "" : v; }

    private String buildCellText(VwStudentSchedule item, Slot slot) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getCourseName() != null ? item.getCourseName() : item.getCourseId());
        if (item.getClassPlace() != null && !item.getClassPlace().isEmpty()) {
            sb.append("\n").append(item.getClassPlace());
        }
        if (item.getTeacherName() != null && !item.getTeacherName().isEmpty()) {
            sb.append(" · ").append(item.getTeacherName());
        }
        sb.append("\n第").append(slot.start).append("-").append(slot.end).append("节");
        return sb.toString();
    }

    /** 优先加载本机可用的中文字体，避免 .notdef */
    private FontPair loadFonts(PDDocument doc) throws IOException {
        String[] candidates = {
                "C:/Windows/Fonts/msyh.ttc",   // 微软雅黑 (TTC)
                "C:/Windows/Fonts/simhei.ttf", // 黑体
                "C:/Windows/Fonts/simsun.ttc", // 宋体 (TTC)
                "/usr/share/fonts/truetype/noto/NotoSansCJK-Regular.ttc"
        };
        for (String path : candidates) {
            File f = new File(path);
            if (!f.exists()) {
                continue;
            }
            try {
                PDType0Font font;
                if (path.toLowerCase().endsWith(".ttc")) {
                    // TTC 字体集合需要指定索引，0 表示第一个字体
                    font = PDType0Font.load(doc, new java.io.FileInputStream(f), true);
                } else {
                    font = PDType0Font.load(doc, f);
                }
                return new FontPair(font, font);
            } catch (IOException ex) {
                // 尝试下一个字体
            }
        }
        // 兜底：使用内置英文字体（中文可能显示为方框）
        return new FontPair(PDType1Font.HELVETICA, PDType1Font.HELVETICA_BOLD);
    }

    private static class FontPair {
        final PDFont regular;
        final PDFont bold;
        FontPair(PDFont regular, PDFont bold) { this.regular = regular; this.bold = bold; }
    }

    private static class Slot {
        final int day; final int start; final int end;
        Slot(int day, int start, int end) { this.day = day; this.start = start; this.end = end; }
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

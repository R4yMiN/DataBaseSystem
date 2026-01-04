<template>
  <div class="app-container">
    <!-- 1. 顶部操作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="课号" prop="courseId">
        <el-input v-model="queryParams.courseId" placeholder="请输入课号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

      <!-- 视图切换：列表 vs 课表 -->
      <el-form-item style="margin-left: 20px">
        <el-radio-group v-model="viewType" size="mini">
          <el-radio-button label="list">列表模式</el-radio-button>
          <el-radio-button label="visual">可视化课表</el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['school:class:add']">老师申请开课</el-button>
      </el-col>
      <el-col :span="1.5" v-if="isAdmin">
        <el-button type="danger" plain icon="el-icon-magic-stick" size="mini" @click="handleRandomKick" v-hasPermi="['school:class:kick']">随机抽签踢人</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 2. 模式 A：列表展示（数据聚合描述） -->
    <div v-if="viewType === 'list'">
      <el-table v-loading="loading" :data="classList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="学期" align="center" prop="semester" />
        <!-- 课号和课名一起显示 -->
        <el-table-column label="课程信息" align="left" width="200">
          <template slot-scope="scope">
            <div style="font-weight: bold;">{{ scope.row.courseName }}</div>
            <div style="font-size: 12px; color: #999;">课号：{{ scope.row.courseId }}</div>
          </template>
        </el-table-column>
        <el-table-column label="班级号/课序" align="center" prop="classSection" />
        <!-- 老师工号和姓名一起显示 -->
        <el-table-column label="任课教师" align="center">
          <template slot-scope="scope">
            <!-- 如果 staffName 有值就显示名字，没值就显示工号保底 -->
            <div style="font-weight: bold;">{{ scope.row.staffName || '未指定' }}</div>
            <div style="font-size: 11px; color: #999;">工号：{{ scope.row.staffId }}</div>
          </template>
        </el-table-column>
        <el-table-column label="上课安排" align="center" min-width="200">
          <template slot-scope="scope">
            <span v-if="scope.row.dayOfWeek">{{ formatTimeDesc(scope.row) }}</span>
            <el-tag v-else type="warning" size="mini">待教务排课</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="地点" align="center" prop="classroomId" />
        <el-table-column label="已选/容量" align="center">
          <template slot-scope="scope">
            <span :style="{color: scope.row.selectedNum > scope.row.capacity ? 'red' : 'inherit'}">
              {{ scope.row.selectedNum }} / {{ scope.row.capacity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button v-if="isAdmin"
                       size="mini"
                       type="text"
                       icon="el-icon-edit"
                       @click="handleUpdate(scope.row)">
              排课
            </el-button>
            <!-- 追加时段按钮 -->
            <el-button
              v-if="isAdmin"
              size="mini"
              type="text"
              icon="el-icon-time"
              @click="handleAppendTime(scope.row)"
            >追加时段</el-button>
            <el-button
              size="isAdmin"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['school:class:remove']">
              删除
            </el-button>

          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 7x12 可视化周课表 -->
    <div v-else class="timetable-container">
      <table class="timetable">
        <thead>
        <tr>
          <th style="width: 80px;">节次</th>
          <th v-for="d in 7" :key="d">周{{ d === 7 ? '日' : ['一','二','三','四','五','六'][d-1] }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="l in 12" :key="l">
          <td class="lesson-header">第 {{l}} 节</td>

          <!-- 找到这一行 -->
          <td v-for="dayIdx in 7" :key="dayIdx" class="course-cell">

            <!-- getCellData 会根据星期和节次，从你刚才 JOIN 出来的结果里提取数据 -->
            <div v-for="item in getCellData(dayIdx, l)" :key="item.classId" class="course-card">

              <!-- 这里改掉：由 item.courseId 改为 item.courseName -->
              <div class="name" style="font-weight: bold; color: #1890ff;">
                {{ item.courseName }}
              </div>

              <!-- 显示地点 -->
              <div class="room" style="font-size: 11px; color: #666;">
                {{ item.classroomId }} 教室
              </div>

            </div>
          </td>


        </tr>
        </tbody>
      </table>
    </div>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 4. 添加或修改开课对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-divider content-position="left">1. 老师选择课程 (计划内)</el-divider>
        <!-- 课程卡片点选区域 -->
        <el-form-item label="点击选择" prop="courseId">
          <el-row :gutter="10" class="course-picker">
            <el-col :span="8" v-for="opt in courseOptions" :key="opt.courseId">
              <div
                :class="['opt-card', form.courseId === opt.courseId ? 'active' : '']"
                @click="form.courseId = opt.courseId"
              >
                <div class="opt-name">{{ opt.courseName }}</div>
                <div class="opt-id">{{ opt.courseId }} ({{ opt.credit }}学分)</div>
              </div>
            </el-col>
          </el-row>
          <div class="selected-tip">当前选中：<el-tag size="small">{{ form.courseId || '请选择' }}</el-tag></div>
        </el-form-item>
        <el-form-item label="班级号/课序" prop="classSection">
          <el-input v-model="form.classSection" placeholder="例如：01班 或 A班" />
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="form.semester" placeholder="请输入学期（如2025秋）" />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" />
        </el-form-item>

        <!-- 只有教务能填 -->
        <div v-if="isAdmin">
          <el-divider content-position="left">2. 教务排课安排 (冲突校验)</el-divider>
          <el-row :gutter="10">
            <el-col :span="8">
              <el-form-item label="星期">
                <el-select v-model="form.dayOfWeek" placeholder="星期">
                  <el-option v-for="d in dictDayOptions" :key="d.value" :label="d.label" :value="d.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="节次">
                <el-input-number v-model="form.lessonStart" :min="1" :max="12" /> -
                <el-input-number v-model="form.lessonEnd" :min="1" :max="12" /> 节
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="安排教室" prop="classroomId">
            <el-input v-model="form.classroomId" placeholder="请输入教室号（如101）" />
          </el-form-item>
        </div>
        <el-alert v-else title="说明：提交后请等待教务处安排具体时间和教室。" type="info" show-icon :closable="false" />
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="submitLoading" :disabled="submitLoading" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import { listClass, getClass, delClass, addClass, updateClass, executeRandomKick } from "@/api/school/class"

export default {
  name: "Class",
  data() {
    return {
      loading: true,
      submitLoading: false,
      viewType: 'list',
      isAdmin: this.$store.getters.roles.includes('admin') || this.$store.getters.roles.includes('dean'),
      courseOptions: [], // 课程库
      dictDayOptions: [{label:'周一',value:1},{label:'周二',value:2},{label:'周三',value:3},{label:'周四',value:4},{label:'周五',value:5},{label:'周六',value:6},{label:'周日',value:7}],
      ids: [], single: true, multiple: true, showSearch: true, total: 0, classList: [], title: "", open: false,
      queryParams: { pageNum: 1, pageSize: 10, courseId: null },
      form: {},
      rules: {
        courseId: [{ required: true, message: "请选择计划内课程", trigger: "change" }],
        semester: [{ required: true, message: "学期不能为空", trigger: "blur" }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    /** 1. 加载开课列表 */
    getList() {
      this.loading = true;
      listClass(this.queryParams).then(res => {
        this.classList = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    /** 2. 加载课程库（可视化点选的核心数据） */
    getCourseLibrary() {
      return request({ url: '/school/class/courseList', method: 'get' }).then(res => {
        this.courseOptions = res.data;
      });
    },
    /** 3. 可视化格子定位 */
    getClassesAt(day, lesson) {
      return this.classList.filter(c => c.dayOfWeek === day && lesson >= c.lessonStart && lesson <= c.lessonEnd);
    },
    /** 4. 数字化转中文描述 */
    formatTimeDesc(row) {
      const days = ["", "周一", "周二", "周三", "周四", "周五", "周六", "周日"];
      return `${days[row.dayOfWeek]} 第${row.lessonStart}-${row.lessonEnd}节`;
    },
    /** 可视化课表逻辑：获取某个单元格（星期几，第几节）的课程数据 */
    getCellData(day, lesson) {
      if (!this.classList || this.classList.length === 0) return [];

      // 过滤出符合当前格子的课：
      // 1. 星期对得上 (dayOfWeek)
      // 2. 当前节次在开始和结束节次之间 (lessonStart <= lesson <= lessonEnd)
      return this.classList.filter(item =>
        item.dayOfWeek === day &&
        lesson >= item.lessonStart &&
        lesson <= item.lessonEnd
      );
    },
    async handleAdd() {
      this.reset();
      await this.getCourseLibrary(); // 弹窗前先抓取正式课程表
      this.open = true;
      this.title = "申请开课";
    },
    handleUpdate(row) {
      this.reset();
      this.getCourseLibrary();
      getClass(row.classId).then(res => {
        this.form = res.data;
        this.open = true;
        this.title = "教务排课安排";
      });
    },
    submitForm() {
      if (this.submitLoading) return;
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          const action = this.form.classId != null ? updateClass : addClass;
          action(this.form).then(() => {
            this.$modal.msgSuccess("提交成功");
            this.open = false;
            this.getList();
          }).finally(() => {
            this.submitLoading = false;
          });
        }
      });
    },
    cancel() { this.open = false; this.reset(); },
    reset() {
      this.form = { classId: null, semester: '2025秋', courseId: null, capacity: 60, dayOfWeek: null, lessonStart: 1, lessonEnd: 2,classSection:null };
      this.resetForm("form");
    },
    handleRandomKick() {
      this.$confirm('确定开始随机抽签？', "警告", { type: "warning" }).then(() => {
        return executeRandomKick();
      }).then(() => { this.getList(); this.$modal.msgSuccess("抽签完成"); });
    },
    handleDelete(row) {
      const ids = row.classId || this.ids;
      this.$modal.confirm('确定删除？').then(() => { return delClass(ids); }).then(() => { this.getList(); });
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) { this.ids = selection.map(i => i.classId); this.single = selection.length!==1; this.multiple = !selection.length; },
    /** 追加时段按钮逻辑 */
    handleAppendTime(row) {
      this.reset();
      // 提取原本的课程信息，放置在新表单里，但时间留空
      this.form = {
        semester: row.semester,
        courseId: row.courseId,
        staffId: row.staffId,
        capacity: row.capacity,
        dayOfWeek: null, // 让教务填新的时间
        lessonStart: 1,
        lessonEnd: 2,
        classroomId: null
      };
      this.open = true;
      this.title = "为此课程追加上课时段";
    }
  }
}
</script>

<style scoped>
/* 课程墙样式 */
.course-picker { max-height: 250px; overflow-y: auto; padding: 10px; border: 1px solid #eee; border-radius: 4px; }
.opt-card { border: 1px solid #ddd; padding: 10px; border-radius: 6px; cursor: pointer; text-align: center; margin-bottom: 10px; transition: 0.2s; }
.opt-card:hover { border-color: #1890ff; background-color: #f0f7ff; }
.opt-card.active { border-color: #1890ff; background-color: #1890ff; color: white; }
.opt-name { font-weight: bold; font-size: 14px; }
.opt-id { font-size: 12px; margin-top: 5px; opacity: 0.8; }
.selected-tip { margin-top: 10px; color: #666; }

/* 课表样式 */
.timetable-container { margin-top: 20px; overflow-x: auto; }
.timetable { width: 100%; border-collapse: collapse; table-layout: fixed; border: 2px solid #333; }
.timetable th, .timetable td { border: 1px solid #ccc; height: 60px; text-align: center; font-size: 12px; }
.timetable th { background: #f4f4f5; font-weight: bold; }
.lesson-header { background: #f4f4f5; font-weight: bold; }
.course-card { background: #e8f4ff; border-left: 4px solid #1890ff; margin: 2px; padding: 4px; border-radius: 3px; }
.course-card .name { font-weight: bold; color: #0056b3; }
</style>

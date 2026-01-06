<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="课号" prop="courseId">
        <el-input v-model="queryParams.courseId" placeholder="请输入课号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: 20px">
        <el-radio-group v-model="viewType" size="mini" @change="handleQuery">
          <el-radio-button label="list">列表模式</el-radio-button>
          <el-radio-button label="visual">可视化课表</el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['school:class:add']">老师申请开课</el-button>
      </el-col>
      <!-- 管理员专用的随机踢人按钮 -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-magic-stick" size="mini" @click="handleRandomKick" v-hasRole="['admin']">随机抽签踢人</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <div v-if="viewType === 'list'">
      <el-table v-loading="loading" :data="classList" border>
        <el-table-column label="学期" align="center" prop="semester" width="100" />
        <el-table-column label="课程信息" align="left" min-width="200">
          <template slot-scope="scope">
            <div style="font-weight: bold;">{{ scope.row.courseName }}</div>
            <div style="font-size: 12px; color: #999;">课号：{{ scope.row.courseId }}</div>
          </template>
        </el-table-column>
        <el-table-column label="教师" align="center" width="120">
          <template slot-scope="scope">{{ scope.row.staffName || '—' }}</template>
        </el-table-column>
        <el-table-column label="班级/性质" align="center" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isPrimary === 0" type="info" size="mini">追加时段</el-tag>
            <span v-else>{{ scope.row.classSection }}</span>
          </template>
        </el-table-column>
        <el-table-column label="安排" align="center" min-width="180">
          <template slot-scope="scope">
            <span v-if="scope.row.dayOfWeek">{{ formatTimeDesc(scope.row) }}</span>
            <el-tag v-else type="warning" size="mini">待排课</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="地点" align="center" prop="classroomId" />
        <el-table-column label="已选/容量" align="center" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.selectedNum >= scope.row.capacity ? 'danger' : 'success'">
              {{ scope.row.selectedNum }} / {{ scope.row.capacity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button v-if="isAdmin" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">排课</el-button>
            <el-button v-if="isAdmin" size="mini" type="text" icon="el-icon-time" @click="handleAppendTime(scope.row)">追加</el-button>
            <el-button v-if="isAdmin" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div v-else class="timetable-container">
      <table class="timetable">
        <thead>
        <tr>
          <th style="width: 80px;">节次</th>
          <th v-for="d in 7" :key="d">周{{ ['一','二','三','四','五','六','日'][d-1] }}</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="l in 12" :key="l">
          <td class="lesson-header">第 {{l}} 节</td>
          <td v-for="dayIdx in 7" :key="dayIdx" class="course-cell">
            <div v-for="item in getCellData(dayIdx, l)" :key="item.classId" class="course-card">
              <div class="name">{{ item.courseName }}</div>
              <div class="room">{{ item.classroomId }} 教室</div>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-divider content-position="left">基础信息</el-divider>
        <el-form-item label="课程选择" prop="courseId">
          <el-select v-model="form.courseId" :disabled="form.isPrimary === 0" style="width:100%">
            <el-option v-for="opt in courseOptions" :key="opt.courseId" :label="opt.courseName" :value="opt.courseId" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="班级号" prop="classSection">
              <el-input v-model="form.classSection" :disabled="form.isPrimary === 0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学期" prop="semester">
              <el-input v-model="form.semester" :disabled="form.isPrimary === 0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-divider content-position="left">排课安排</el-divider>
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="星期" prop="dayOfWeek">
              <el-select v-model="form.dayOfWeek">
                <el-option v-for="d in dictDayOptions" :key="d.value" :label="d.label" :value="d.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="节次范围">
              <el-input-number v-model="form.lessonStart" :min="1" :max="12" @change="onLessonStartChange" /> -
              <el-input-number v-model="form.lessonEnd" :min="form.lessonStart || 1" :max="12" /> 节
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="课程容量" prop="capacity" v-if="isAdmin">
          <el-input-number v-model="form.capacity" :min="1" style="width:100%" placeholder="请输入学生容量" />
        </el-form-item>
        <el-form-item label="地点" prop="classroomId">
          <el-select v-model="form.classroomId" filterable placeholder="请选择教室" style="width:100%">
            <el-option v-for="room in classroomOptions" :key="room.classroomId" :label="room.roomName" :value="room.roomName" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import { listClass, getClass, delClass, addClass, updateClass, listClassroom, executeRandomKick } from "@/api/school/class"

export default {
  name: "Class",
  data() {
    return {
      loading: true,
      showSearch: true,
      viewType: 'list',
      isAdmin: this.$store.getters.roles.includes('admin') || this.$store.getters.roles.includes('dean'),
      courseOptions: [],
      classroomOptions: [],
      dictDayOptions: [{label:'周一',value:1},{label:'周二',value:2},{label:'周三',value:3},{label:'周四',value:4},{label:'周五',value:5},{label:'周六',value:6},{label:'周日',value:7}],
      total: 0,
      classList: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10 },
      form: {},
      rules: { courseId: [{ required: true, message: "必填", trigger: "change" }], semester: [{ required: true, message: "必填", trigger: "blur" }] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true;
      this.queryParams.params = { viewType: this.viewType };
      listClass(this.queryParams).then(res => {
        this.classList = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    handleRandomKick() {
      this.$modal.confirm('确定要对所有班级进行随机抽签剔除吗？超过容量的学生将被随机移除。').then(() => {
        this.loading = true;
        return executeRandomKick();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("随机抽签执行完成！");
      }).catch(() => {
        this.loading = false;
      });
    },
    formatTimeDesc(row) {
      const days = ["", "周一", "周二", "周三", "周四", "周五", "周六", "周日"];
      return `${days[row.dayOfWeek]} 第${row.lessonStart}-${row.lessonEnd}节`;
    },
    getCellData(day, lesson) {
      if (!this.classList) return [];
      return this.classList.filter(item =>
        item.dayOfWeek === day && lesson >= item.lessonStart && lesson <= item.lessonEnd
      );
    },
    getCourseLibrary() { return request({ url: '/school/class/courseList', method: 'get' }).then(res => { this.courseOptions = res.data; }); },
    getClassroomList() { return listClassroom().then(res => { this.classroomOptions = res.data; }); },
    async handleAdd() {
      this.reset();
      await this.getCourseLibrary();
      await this.getClassroomList();
      this.form.isPrimary = 1;
      this.open = true;
      this.title = "老师申请开课";
    },
    handleUpdate(row) {
      this.reset();
      this.getCourseLibrary();
      this.getClassroomList();
      getClass(row.classId).then(res => { this.form = res.data; this.open = true; this.title = "教务排课"; });
    },
    handleAppendTime(row) {
      this.reset();
      this.getCourseLibrary();
      this.getClassroomList();
      this.form = { ...row, classId: null, isPrimary: 0, dayOfWeek: null, lessonStart: 1, lessonEnd: 2, classroomId: null };
      this.open = true;
      this.title = "追加时段";
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const action = this.form.classId != null ? updateClass : addClass;
          action(this.form).then(() => { this.$modal.msgSuccess("操作成功"); this.open = false; this.getList(); });
        }
      });
    },
    cancel() { this.open = false; },
    reset() { this.form = { classId: null, isPrimary: 1, semester: '2025秋', lessonStart: 1, lessonEnd: 2 }; },
    onLessonStartChange(val) {
      if (this.form.lessonEnd < val) {
        this.form.lessonEnd = val;
      }
    },
    handleDelete(row) {
      this.$modal.confirm('确认删除？如果是主课，关联追加时段也会被删除。').then(() => {
        return delClass(row.classId);
      }).then(() => { this.getList(); this.$modal.msgSuccess("删除成功"); });
    },
    handleQuery() { this.getList(); },
    resetQuery() { this.queryParams = { pageNum: 1, pageSize: 10 }; this.handleQuery(); }
  }
}
</script>

<style scoped>
.timetable { width: 100%; border-collapse: collapse; border: 1px solid #ddd; margin-top: 20px; table-layout: fixed; }
.timetable th, .timetable td { border: 1px solid #ddd; height: 60px; text-align: center; font-size: 12px; }
.timetable th { background: #f5f7fa; font-weight: bold; }
.lesson-header { background: #f5f7fa; font-weight: bold; width: 60px; }
.course-card { background: #e8f4ff; border-left: 3px solid #1890ff; padding: 4px; border-radius: 2px; font-weight: bold; color: #1890ff; margin-bottom: 2px; }
</style>

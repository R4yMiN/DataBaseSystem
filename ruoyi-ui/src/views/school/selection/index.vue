<template>
  <div class="app-container">
    <!-- 1. 顶部卡片：我的已选课程（课表视角） -->
    <el-card class="mb16" shadow="never" style="margin-bottom: 20px;">
      <div slot="header" style="font-weight: bold;">
        <i class="el-icon-date"></i> 我的已选课程
      </div>
      <el-table v-loading="loadingSchedule" :data="mySchedule" size="small" border>
        <el-table-column label="课程代码" align="center" prop="courseId" width="100" />
        <el-table-column label="课程名称" align="center" prop="courseName" />
        <el-table-column label="班级号" align="center" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.courseId }}</span> /
            <el-tag size="mini" type="success">{{ scope.row.classSection }}</el-tag>
          </template>
        </el-table-column>
        <!-- 教师姓名 (及教工号) -->
        <el-table-column label="教师" align="center" width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.teacherName }}</span><br/>
            <span style="color: #999; font-size: 12px">({{ scope.row.staffId }})</span>
          </template>
        </el-table-column>
        <el-table-column label="教师工号" align="center" prop="staffId" />
        <el-table-column label="学期" align="center" prop="semester" width="120" />
        <el-table-column label="班级/课序" align="center" prop="classSection" width="100" />
        <el-table-column label="上课时间" align="center" min-width="180">
          <template slot-scope="scope">
            <div v-for="time in (scope.row.classTime || '').split('|')" :key="time">
              <el-tag size="mini" style="margin-bottom: 2px;">{{ time }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="上课地点" align="center" prop="classroomId" />
        <el-table-column label="操作" align="center" width="100">
          <template slot-scope="scope">
            <el-button type="text" style="color: #f56c6c" icon="el-icon-circle-close" @click="handleUnselect(scope.row)">退选</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 2. 查询表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="教师姓名" prop="teacherName">
        <el-input v-model="queryParams.teacherName" placeholder="请输入教师姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 3. 数据表格：选课大厅 -->
    <el-table v-loading="loading" :data="selectionList" border>
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="教师姓名" align="center" prop="teacherName" width="100" />
      <el-table-column label="班级/课序" align="center" prop="classSection" width="100" />
      <el-table-column label="学期" align="center" prop="semester" width="100" />
      <el-table-column label="上课时间" align="center" min-width="200">
        <template slot-scope="scope">
          <div v-for="time in (scope.row.classTime || '').split('|')" :key="time">
            <el-tag size="mini" type="info" style="margin-bottom: 2px;">{{ time }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="已选/容量" align="center" width="100">
        <template slot-scope="scope">
          {{ scope.row.selectedNum }} / {{ scope.row.capacity }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- 学生视角 -->
          <div v-if="!isAdmin">
            <el-button
              v-if="scope.row.isSelected === 1"
              size="mini"
              type="danger"
              @click="handleUnselect(scope.row)"
            >退选</el-button>
            <el-button
              v-else
              size="mini"
              type="primary"
              :disabled="scope.row.selectedNum >= scope.row.capacity"
              @click="handleSelect(scope.row)"
            >选择</el-button>
          </div>
          <!-- 管理员视角 -->
          <div v-else>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">管理</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 管理对话框（评分或调整） -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="课程名称">{{form.courseName}}</el-form-item>
        <el-form-item label="平时成绩">
          <el-input-number v-model="form.normalScore" :min="0" :max="100" />
        </el-form-item>
        <el-form-item label="考试成绩">
          <el-input-number v-model="form.testScore" :min="0" :max="100" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 注意：API 路径必须对应我们之前修改的 /school/selection
import { listSelection, getSelection, delSelection, addSelection, updateSelection, getMySchedule } from "@/api/system/selection"

export default {
  name: "Selection",
  data() {
    return {
      loading: true,
      loadingSchedule: false,
      showSearch: true,
      total: 0,
      isAdmin: this.$store.getters.roles.includes('admin') || this.$store.getters.roles.includes('dean'),
      selectionList: [], // 下方大厅数据
      mySchedule: [],    // 顶部已选数据
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseName: undefined,
        teacherName: undefined
      },
      form: {}
    }
  },
  created() {
    this.getList();
    this.loadMySchedule();
  },
  methods: {
    /** 查询选课大厅 */
    getList() {
      this.loading = true;
      listSelection(this.queryParams).then(res => {
        this.selectionList = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    /** 加载我的课表 */
    loadMySchedule() {
      this.loadingSchedule = true;
      getMySchedule().then(res => {
        this.mySchedule = res.rows;
        this.loadingSchedule = false;
      }).catch(() => {
        this.loadingSchedule = false;
      });
    },
    /** 选课操作 */
    handleSelect(row) {
      this.$modal.confirm(`确认要选择《${row.courseName}》吗？这将选中该班级的所有教学时段。`).then(() => {
        // 关键：传给后端的必须是 classId (即聚合后的代表ID)
        return addSelection({ classId: row.classId });
      }).then(() => {
        this.$modal.msgSuccess("选课成功");
        this.getList();
        this.loadMySchedule();
      });
    },
    /** 退选操作 */
    handleUnselect(row) {
      this.$modal.confirm(`确认要退选《${row.courseName}》吗？该班级所有时段将被移除。`).then(() => {
        // 关键：退选使用的是选课记录的唯一主键 selectionId
        return delSelection(row.selectionId);
      }).then(() => {
        this.$modal.msgSuccess("退选成功");
        this.getList();
        this.loadMySchedule();
      });
    },
    /** 管理按钮 */
    handleUpdate(row) {
      this.form = { ...row };
      this.open = true;
      this.title = "管理选课/评分";
    },
    /** 提交管理表单 */
    submitForm() {
      updateSelection(this.form).then(response => {
        this.$modal.msgSuccess("操作成功");
        this.open = false;
        this.getList();
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    cancel() {
      this.open = false;
    },
    handleDelete(row) {
      this.$modal.confirm('确认强制删除该选课记录？').then(() => {
        return delSelection(row.selectionId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      });
    }
  }
}
</script>

<style scoped>
.mb16 { margin-bottom: 16px; }
</style>

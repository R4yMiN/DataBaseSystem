<template>
  <div class="app-container">
    <!-- ============ 学生视角 ============ -->
    <template v-if="!isAdmin">
      <!-- 1. 顶部卡片：我的已选课程 -->
      <el-card class="mb16" shadow="never" style="margin-bottom: 20px;">
        <div slot="header" style="font-weight: bold;">
          <i class="el-icon-date"></i> 我的已选课程
        </div>
        <el-table v-loading="loadingSchedule" :data="mySchedule" size="small" border>
          <el-table-column label="课程代码" align="center" prop="courseId" width="100" />
          <el-table-column label="课程名称" align="center" prop="courseName" />
          <el-table-column label="教师" align="center" width="120">
            <template slot-scope="scope">
              <span>{{ scope.row.teacherName }}</span>
            </template>
          </el-table-column>
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
            <span :style="{ color: scope.row.selectedNum >= scope.row.capacity ? '#f56c6c' : '' }">
              {{ scope.row.selectedNum }} / {{ scope.row.capacity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template slot-scope="scope">
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
    </template>

    <!-- ============ 管理员/教师视角：成绩录入 ============ -->
    <template v-else>
      <el-card shadow="never">
        <div slot="header" style="font-weight: bold;">
          <i class="el-icon-edit-outline"></i> 成绩录入管理
        </div>
        
        <!-- 步骤1：选择课程 -->
        <el-form :inline="true" size="small" style="margin-bottom: 15px;">
          <el-form-item label="选择课程">
            <el-select 
              v-model="selectedCourseId" 
              filterable 
              placeholder="请选择课程"
              style="width: 350px;"
              @change="handleCourseChange"
            >
              <el-option 
                v-for="course in courseList" 
                :key="course.classId" 
                :label="`${course.courseName} - ${course.teacherName || ''} (${course.classSection || ''})`"
                :value="course.classId"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="loadStudentList" :disabled="!selectedCourseId">查询选课学生</el-button>
          </el-form-item>
        </el-form>

        <!-- 步骤2：设置成绩比例 -->
        <el-card v-if="selectedCourseId" shadow="never" style="margin-bottom: 15px; background: #f5f7fa;">
          <el-form :inline="true" size="small">
            <el-form-item label="成绩比例设置">
              <span style="margin-right: 10px;">平时 {{ normalRatio }}%</span>
              <el-slider 
                v-model="normalRatio" 
                :min="0" 
                :max="100" 
                :step="5"
                style="width: 200px; display: inline-block;"
                @input="testRatio = 100 - normalRatio"
              />
              <span style="margin-left: 10px;">考试 {{ testRatio }}%</span>
            </el-form-item>
          </el-form>
        </el-card>

        <!-- 步骤3：显示选课学生列表并录入成绩 -->
        <el-table v-loading="loadingStudents" :data="studentList" border v-show="studentList.length > 0 || loadingStudents">
          <el-table-column label="学号" align="center" prop="studentId" width="150" />
          <el-table-column label="姓名" align="center" prop="studentName" width="120" />
          <el-table-column align="center" width="180">
            <template slot="header">
              平时成绩<el-tag size="mini" type="info" style="margin-left: 5px;">{{ normalRatio }}%</el-tag>
            </template>
            <template slot-scope="scope">
              <el-input-number 
                v-model="scope.row.normalScore" 
                :min="0" 
                :max="100" 
                size="small"
                controls-position="right"
                style="width: 100px;"
              />
            </template>
          </el-table-column>
          <el-table-column align="center" width="150">
            <template slot="header">
              考试成绩<el-tag size="mini" type="info" style="margin-left: 5px;">{{ testRatio }}%</el-tag>
            </template>
            <template slot-scope="scope">
              <el-input-number 
                v-model="scope.row.testScore" 
                :min="0" 
                :max="100" 
                size="small"
                controls-position="right"
                style="width: 100px;"
              />
            </template>
          </el-table-column>
          <el-table-column label="总成绩" align="center" width="100">
            <template slot-scope="scope">
              <span :style="{ color: calcTotal(scope.row) < 60 ? '#f56c6c' : '#67c23a', fontWeight: 'bold' }">
                {{ calcTotal(scope.row) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="100">
            <template slot-scope="scope">
              <el-button type="text" icon="el-icon-check" @click="saveStudentScore(scope.row)">保存</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="studentList.length === 0 && !loadingStudents && selectedCourseId" style="text-align: center; padding: 40px; color: #999;">
          <i class="el-icon-warning-outline" style="font-size: 48px;"></i>
          <p>该课程暂无学生选课</p>
        </div>

        <div v-if="studentList.length > 0" style="margin-top: 15px; text-align: right;">
          <el-button type="success" icon="el-icon-check" @click="saveAllScores">批量保存所有成绩</el-button>
        </div>
      </el-card>
    </template>
  </div>
</template>

<script>
import { listSelection, delSelection, addSelection, updateSelection, getMySchedule } from "@/api/system/selection"
import request from '@/utils/request'

export default {
  name: "Selection",
  data() {
    return {
      loading: true,
      loadingSchedule: false,
      loadingStudents: false,
      showSearch: true,
      total: 0,
      isAdmin: this.$store.getters.roles.includes('admin') || this.$store.getters.roles.includes('dean') || this.$store.getters.roles.includes('teacher'),
      selectionList: [],
      mySchedule: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseName: undefined,
        teacherName: undefined
      },
      // 管理员成绩录入相关
      courseList: [],
      selectedCourseId: null,
      studentList: [],
      // 成绩比例设置
      normalRatio: 40,  // 平时成绩占比40%
      testRatio: 60     // 考试成绩占比60%
    }
  },
  created() {
    if (this.isAdmin) {
      this.loadCourseList();
    } else {
      this.getList();
      this.loadMySchedule();
    }
  },
  methods: {
    /** 学生：查询选课大厅 */
    getList() {
      this.loading = true;
      listSelection(this.queryParams).then(res => {
        this.selectionList = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    /** 学生：加载我的课表 */
    loadMySchedule() {
      this.loadingSchedule = true;
      getMySchedule().then(res => {
        this.mySchedule = res.rows;
        this.loadingSchedule = false;
      }).catch(() => {
        this.loadingSchedule = false;
      });
    },
    /** 学生：选课操作 */
    handleSelect(row) {
      this.$modal.confirm(`确认要选择《${row.courseName}》吗？`).then(() => {
        return addSelection({ classId: row.classId });
      }).then(() => {
        this.$modal.msgSuccess("选课成功");
        this.getList();
        this.loadMySchedule();
      });
    },
    /** 学生：退选操作 */
    handleUnselect(row) {
      this.$modal.confirm(`确认要退选《${row.courseName}》吗？`).then(() => {
        return delSelection(row.selectionId);
      }).then(() => {
        this.$modal.msgSuccess("退选成功");
        this.getList();
        this.loadMySchedule();
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
    
    // ========== 管理员成绩录入方法 ==========
    /** 加载课程列表供选择（只加载主课程） */
    loadCourseList() {
      request({
        url: '/school/class/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 500, isPrimary: 1 }
      }).then(res => {
        this.courseList = res.rows || [];
      });
    },
    /** 课程选择变化时 */
    handleCourseChange(classId) {
      this.studentList = [];
      if (classId) {
        this.loadStudentList();
      }
    },
    /** 加载选择该课程的学生列表 */
    loadStudentList() {
      if (!this.selectedCourseId) return;
      this.loadingStudents = true;
      // 查询选了这门课的所有学生
      request({
        url: '/school/selection/students',
        method: 'get',
        params: { classId: this.selectedCourseId }
      }).then(res => {
        this.studentList = res.rows || res.data || [];
        this.loadingStudents = false;
      }).catch(() => {
        this.loadingStudents = false;
      });
    },
    /** 计算总成绩 (根据自定义比例) */
    calcTotal(row) {
      const normal = row.normalScore || 0;
      const test = row.testScore || 0;
      const nRatio = this.normalRatio / 100;
      const tRatio = this.testRatio / 100;
      return Math.round(normal * nRatio + test * tRatio);
    },
    /** 保存单个学生成绩 */
    saveStudentScore(row) {
      updateSelection({
        selectionId: row.selectionId,
        normalScore: row.normalScore,
        testScore: row.testScore,
        totalScore: this.calcTotal(row)
      }).then(() => {
        this.$modal.msgSuccess("保存成功");
      });
    },
    /** 批量保存所有成绩 */
    saveAllScores() {
      const promises = this.studentList.map(row => {
        return updateSelection({
          selectionId: row.selectionId,
          normalScore: row.normalScore,
          testScore: row.testScore,
          totalScore: this.calcTotal(row)
        });
      });
      Promise.all(promises).then(() => {
        this.$modal.msgSuccess("批量保存成功");
      }).catch(() => {
        this.$modal.msgError("部分保存失败，请检查");
      });
    }
  }
}
</script>

<style scoped>
.mb16 { margin-bottom: 16px; }
</style>

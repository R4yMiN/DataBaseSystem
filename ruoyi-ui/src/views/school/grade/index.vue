<template>
  <div class="app-container grade-container">
    <!-- 顶部卡片：学期选择 + 统计信息 -->
    <el-card class="filter-card" shadow="hover">
      <div class="filter-header">
        <div class="filter-left">
          <i class="el-icon-trophy trophy-icon"></i>
          <span class="title">我的成绩单</span>
        </div>
        <div class="filter-right">
          <span class="filter-label">选择学期：</span>
          <el-select 
            v-model="queryParams.semester" 
            placeholder="全部学期" 
            clearable 
            style="width: 160px"
            @change="handleQuery">
            <el-option 
              v-for="sem in semesterOptions" 
              :key="sem" 
              :label="sem" 
              :value="sem" />
          </el-select>
        </div>
      </div>
      
      <!-- 统计信息 -->
      <div class="stats-row" v-if="gradeList.length > 0">
        <div class="stat-item">
          <div class="stat-value">{{ gradeList.length }}</div>
          <div class="stat-label">已修课程</div>
        </div>
        <div class="stat-item">
          <div class="stat-value primary">{{ totalCredits }}</div>
          <div class="stat-label">总学分</div>
        </div>
        <div class="stat-item">
          <div class="stat-value" :class="avgGpaClass">{{ avgGpa }}</div>
          <div class="stat-label">加权平均绩点</div>
        </div>
      </div>
    </el-card>

    <!-- 成绩列表 -->
    <el-card class="grade-card" shadow="hover">
      <div slot="header" class="card-header">
        <span><i class="el-icon-document"></i> 成绩明细</span>
        <el-tag v-if="queryParams.semester" type="primary" size="small">
          {{ queryParams.semester }}
        </el-tag>
      </div>
      
      <el-table 
        v-loading="loading" 
        :data="gradeList" 
        border 
        stripe
        show-summary
        :summary-method="getSummaries"
        :header-cell-style="{background:'#f5f7fa', color:'#606266', fontWeight:'bold'}"
        empty-text="暂无成绩记录">
        <el-table-column label="学期" align="center" prop="semester" width="100">
          <template slot-scope="scope">
            <el-tag size="small" type="info">{{ scope.row.semester }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="课程号" align="center" prop="courseId" width="100" />
        <el-table-column label="课程名称" align="center" prop="courseName" min-width="160">
          <template slot-scope="scope">
            <span class="course-name">{{ scope.row.courseName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="学分" align="center" prop="credit" width="80">
          <template slot-scope="scope">
            <el-tag size="mini" type="primary">{{ scope.row.credit || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="任课教师" align="center" prop="teacherName" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.teacherName || '—' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总成绩" align="center" width="100">
          <template slot-scope="scope">
            <template v-if="scope.row.totalScore != null">
              <span 
                class="score-value" 
                :class="getScoreClass(scope.row.totalScore)">
                {{ scope.row.totalScore }}
              </span>
            </template>
            <template v-else>
              <el-tag size="mini" type="warning">待录入</el-tag>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="绩点" align="center" width="80">
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.totalScore != null"
              :type="getGpaTagType(getGpa(scope.row.totalScore))"
              size="small">
              {{ getGpa(scope.row.totalScore).toFixed(1) }}
            </el-tag>
            <span v-else>—</span>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>
  </div>
</template>

<script>
import { getMyGrades } from "@/api/school/grade"

export default {
  name: "StudentGrade",
  data() {
    return {
      loading: true,
      total: 0,
      gradeList: [],
      semesterOptions: ['2024春', '2024秋', '2025春', '2025秋', '2026春', '2026秋'],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        semester: null
      }
    }
  },
  computed: {
    // 总学分
    totalCredits() {
      return this.gradeList.reduce((acc, g) => acc + (Number(g.credit) || 0), 0)
    },
    // 已出成绩的课程（用于计算加权平均）
    gradedList() {
      return this.gradeList.filter(g => g.totalScore != null)
    },
    // 加权平均绩点 = Σ(学分 × 绩点) / Σ学分
    avgGpa() {
      const graded = this.gradedList
      if (graded.length === 0) return '—'
      const totalCredits = graded.reduce((acc, g) => acc + (Number(g.credit) || 0), 0)
      if (totalCredits === 0) return '—'
      const weightedSum = graded.reduce((acc, g) => {
        const gpa = this.getGpa(g.totalScore)
        const credit = Number(g.credit) || 0
        return acc + gpa * credit
      }, 0)
      return (weightedSum / totalCredits).toFixed(2)
    },
    // 加权平均绩点样式
    avgGpaClass() {
      const gpa = parseFloat(this.avgGpa)
      if (isNaN(gpa)) return ''
      if (gpa >= 3.5) return 'excellent'
      if (gpa >= 3.0) return 'good'
      if (gpa >= 2.0) return 'pass'
      return 'fail'
    },
    // 平均分
    avgScore() {
      const graded = this.gradedList
      if (graded.length === 0) return '—'
      const sum = graded.reduce((acc, g) => acc + Number(g.totalScore), 0)
      return (sum / graded.length).toFixed(1)
    },
    // 平均分样式
    avgScoreClass() {
      const avg = parseFloat(this.avgScore)
      if (isNaN(avg)) return ''
      if (avg >= 90) return 'excellent'
      if (avg >= 80) return 'good'
      if (avg >= 60) return 'pass'
      return 'fail'
    },
    // 最高分
    highestScore() {
      const graded = this.gradedList
      if (graded.length === 0) return '—'
      return Math.max(...graded.map(g => Number(g.totalScore)))
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getMyGrades(this.queryParams).then(response => {
        this.gradeList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // 成绩样式
    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 60) return 'score-pass'
      return 'score-fail'
    },
    // 4.0 GPA（标准五档换算）
    getGpa(score) {
      const s = Number(score)
      if (Number.isNaN(s)) return 0
      if (s >= 90) return 4.0
      if (s >= 80) return 3.0
      if (s >= 70) return 2.0
      if (s >= 60) return 1.0
      return 0.0
    },
    getGpaTagType(gpa) {
      if (gpa >= 3.0) return 'success'
      if (gpa >= 2.0) return ''
      if (gpa >= 1.0) return 'warning'
      return 'danger'
    },
    // 表格合计行
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (column.property === 'credit') {
          sums[index] = this.totalCredits + ' 学分'
          return
        }
        sums[index] = ''
      })
      return sums
    }
  }
}
</script>

<style scoped>
.grade-container {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 15px;
}

.filter-left {
  display: flex;
  align-items: center;
}

.trophy-icon {
  font-size: 28px;
  color: #f5a623;
  margin-right: 10px;
}

.filter-left .title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.filter-right {
  display: flex;
  align-items: center;
}

.filter-label {
  color: #606266;
  margin-right: 8px;
}

.stats-row {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
  flex-wrap: wrap;
}

.stat-item {
  text-align: center;
  padding: 10px 20px;
  min-width: 100px;
}

.stat-value {
  font-size: 26px;
  font-weight: bold;
  color: #409eff;
}

.stat-value.primary { color: #409eff; }
.stat-value.excellent { color: #67c23a; }
.stat-value.good { color: #409eff; }
.stat-value.pass { color: #e6a23c; }
.stat-value.fail { color: #f56c6c; }
.stat-value.highlight { color: #f5a623; }

.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 5px;
}

.grade-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: bold;
}

.course-name {
  font-weight: 500;
}

.score-value {
  font-size: 16px;
  font-weight: bold;
}

.credit-gpa {
  font-weight: 500;
  color: #606266;
}

.score-excellent { color: #67c23a; }
.score-good { color: #409eff; }
.score-pass { color: #e6a23c; }
.score-fail { color: #f56c6c; }
</style>

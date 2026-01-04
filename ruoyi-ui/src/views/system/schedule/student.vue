<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="学生学号" prop="studentId">
        <el-input v-model="queryParams.studentId" placeholder="请输入学生学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学期" prop="semester">
        <el-input v-model="queryParams.semester" placeholder="请输入学期" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:schedule:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <div class="timetable">
      <div class="tt-header">
        <div class="tt-cell tt-corner">节次</div>
        <div v-for="d in 7" :key="d" class="tt-cell tt-day">周{{ dayNames[d-1] }}</div>
      </div>
      <div class="tt-body">
        <div v-for="(row, rowIdx) in 12" :key="rowIdx" class="tt-row">
          <div class="tt-cell tt-slot">
            <div class="slot-no">第 {{ rowIdx + 1 }} 节</div>
            <div class="slot-period">{{ periodName(rowIdx) }}</div>
          </div>
          <div v-for="d in 7" :key="d" class="tt-cell tt-course">
            <template v-if="grid[rowIdx][d-1] && grid[rowIdx][d-1].length">
              <div v-for="(c, idx) in grid[rowIdx][d-1]" :key="idx" class="course-card">
                <div class="course-name">{{ c.courseName }}</div>
                <div class="course-meta">{{ c.classPlace || '待定地点' }} · {{ c.teacherName || '待定教师' }}</div>
                <div class="course-time">{{ c.time }}</div>
              </div>
            </template>
            <div v-else class="empty">—</div>
          </div>
        </div>
      </div>
    </div>

    <div class="pending" v-if="pendingList.length">
      <div class="pending-title">待排课（无上课时间的课程）</div>
      <el-table :data="pendingList" size="small" border>
        <el-table-column label="课程名称" prop="courseName" align="center" />
        <el-table-column label="教师" prop="teacherName" align="center" width="120" />
        <el-table-column label="学期" prop="semester" align="center" width="100" />
        <el-table-column label="上课地点" prop="classPlace" align="center" width="120" />
        <el-table-column label="上课时间" prop="classTime" align="center" width="160" />
      </el-table>
    </div>
  </div>
</template>

<script>
import { getMySchedule } from '@/api/system/selection'

export default {
  name: 'StudentSchedule',
  data() {
    return {
      loading: false,
      showSearch: true,
      scheduleList: [],
      pendingList: [],
      grid: Array.from({ length: 12 }, () => Array.from({ length: 7 }, () => [])),
      dayNames: ['一', '二', '三', '四', '五', '六', '日'],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentId: '',
        semester: ''
      }
    }
  },
  created() {
    const studentId = this.$store?.state?.user?.userName
    if (studentId) {
      this.queryParams.studentId = studentId
    }
    this.getList()
  },
  activated() {
    // 页面被激活时（从其他标签页切换回来）刷新课表数据
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getMySchedule().then(res => {
        this.scheduleList = res.rows || []
        this.buildGrid()
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        studentId: '',
        semester: ''
      }
      const studentId = this.$store?.state?.user?.userName
      if (studentId) {
        this.queryParams.studentId = studentId
      }
      this.getList()
    },
    handleExport() {
      this.download('system/schedule/export', { ...this.queryParams }, `student_schedule_${new Date().getTime()}.xlsx`)
    },
    buildGrid() {
      const emptyGrid = Array.from({ length: 12 }, () => Array.from({ length: 7 }, () => []))
      const pending = []
      this.scheduleList.forEach(item => {
        const slots = this.parseClassTime(item.classTime)
        if (!slots.length) {
          pending.push(item)
          return
        }
        slots.forEach(s => {
          const card = {
            courseName: item.courseName || item.courseId,
            teacherName: item.teacherName,
            classPlace: item.classroomId || item.classPlace,
            time: `第${s.start}-${s.end}节`
          }
          for (let p = s.start; p <= s.end && p <= 12; p++) {
            const rowIdx = p - 1
            const dayIdx = s.day - 1
            if (rowIdx >= 0 && dayIdx >= 0 && dayIdx < 7) {
              emptyGrid[rowIdx][dayIdx].push(card)
            }
          }
        })
      })
      this.grid = emptyGrid
      this.pendingList = pending
    },
    parseClassTime(classTime) {
      if (!classTime) return []
      const parts = classTime.split('|').map(p => p.trim()).filter(Boolean)
      // 支持中文数字（周一、周二）和阿拉伯数字（周1、周2）
      const dayMapCN = { '一': 1, '二': 2, '三': 3, '四': 4, '五': 5, '六': 6, '日': 7, '天': 7 }
      const slots = []
      // 正则同时匹配 "周一" 或 "周1" 或 "周日"
      const regex = /周([一二三四五六日天]|\d)[^\d]*?(\d+)(?:[-~至到—](\d+))?/;
      parts.forEach(p => {
        const m = p.match(regex)
        if (m) {
          let day = 0
          if (dayMapCN[m[1]]) {
            day = dayMapCN[m[1]]
          } else {
            day = Number(m[1]) || 0
          }
          const start = Number(m[2]) || 1
          const end = Number(m[3] || m[2]) || start
          if (day >= 1 && day <= 7) {
            slots.push({ day, start, end })
          }
        }
      })
      return slots
    },
    periodName(rowIdx) {
      if (rowIdx < 4) return '上午'
      if (rowIdx < 8) return '下午'
      return '晚上'
    }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 8px; }
.timetable {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}
.tt-header, .tt-row {
  display: grid;
  grid-template-columns: 120px repeat(7, 1fr);
}
.tt-cell {
  border-bottom: 1px solid #f0f2f5;
  border-right: 1px solid #f0f2f5;
  padding: 8px;
  box-sizing: border-box;
}
.tt-header .tt-cell {
  background: #fafafa;
  font-weight: 600;
  text-align: center;
}
.tt-corner {
  border-right: 1px solid #f0f2f5;
}
.tt-day {
  border-right: 1px solid #f0f2f5;
}
.tt-body .tt-row:last-child .tt-cell {
  border-bottom: none;
}
.tt-body .tt-row .tt-cell:last-child {
  border-right: none;
}
.tt-slot {
  background: #fafafa;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 13px;
}
.slot-no {
  font-weight: 600;
}
.slot-period {
  color: #909399;
}
.tt-course {
  min-height: 70px;
  background: #fff;
}
.empty {
  color: #dcdfe6;
  text-align: center;
  padding: 10px 0;
}
.course-card {
  border-radius: 6px;
  background: #f5f7fa;
  padding: 6px 8px;
  margin-bottom: 6px;
  box-shadow: inset 0 0 0 1px #ebeef5;
}
.course-card:last-child { margin-bottom: 0; }
.course-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 2px;
}
.course-meta, .course-time {
  font-size: 12px;
  color: #606266;
}
.pending {
  margin-top: 16px;
}
.pending-title {
  font-weight: 600;
  margin-bottom: 8px;
}
</style>

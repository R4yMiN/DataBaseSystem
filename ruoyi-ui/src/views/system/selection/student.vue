<template>
<div class="app-container">
<!-- 已选课程简易课表 -->
<el-card class="mb16" shadow="never" style="margin-bottom: 20px;">
<div slot="header" style="font-weight: bold;">
<i class="el-icon-date"></i> 我的已选课程
</div>
<el-table v-loading="loadingSchedule" :data="mySchedule" size="small" border>
<el-table-column label="课程名称" align="center" prop="courseName" />
<el-table-column label="课程ID/课号" align="center" prop="courseId" width="100" />
<el-table-column label="学分" align="center" prop="credit" width="70">
<template slot-scope="scope">
<el-tag size="mini" type="primary">{{ scope.row.credit || 0 }}</el-tag>
</template>
</el-table-column>
<el-table-column label="教师" align="center" prop="teacherName" width="100" />
<el-table-column label="班级ID" align="center" prop="classSection" width="80" />
<el-table-column label="学期" align="center" prop="semester" width="100" />
<el-table-column label="上课时间" align="center" width="180">
<template slot-scope="scope">
<div class="time-slots">
<div v-for="(slot, idx) in parseTimeSlots(scope.row.classTime)" :key="idx" class="time-slot-item">
<el-tag size="mini" :type="idx === 0 ? 'primary' : 'info'" effect="plain">
{{ slot }}
</el-tag>
</div>
</div>
</template>
</el-table-column>
<el-table-column label="操作" align="center" width="80">
<template slot-scope="scope">
<el-button type="danger" size="mini" @click="handleUnselect(scope.row)">退课</el-button>
</template>
</el-table-column>
</el-table>
<div v-if="mySchedule.length > 0" style="text-align: right; margin-top: 10px; padding-right: 10px;">
<span style="font-size: 14px; color: #606266;">已选学分合计：</span>
<el-tag type="success" size="medium">{{ totalSelectedCredits }}</el-tag>
</div>
<div v-if="mySchedule.length === 0 && !loadingSchedule" style="text-align: center; color: #999; padding: 20px;">
暂无已选课程
</div>
</el-card>

<el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
<el-form-item label="课程名称" prop="courseName">
<el-input
v-model="queryParams.courseName"
placeholder="请输入课程名称"
clearable
@keyup.enter.native="handleQuery"
/>
</el-form-item>
<el-form-item label="教师姓名" prop="teacherName">
<el-input
v-model="queryParams.teacherName"
placeholder="请输入教师姓名"
clearable
@keyup.enter.native="handleQuery"
/>
</el-form-item>
<el-form-item label="学期" prop="semester">
<el-input
v-model="queryParams.semester"
placeholder="请输入学期"
clearable
@keyup.enter.native="handleQuery"
/>
</el-form-item>
<el-form-item>
<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
</el-form-item>
</el-form>

<el-table v-loading="loading" :data="selectionList">
<el-table-column label="课程名称" align="center" prop="courseName" />
<el-table-column label="课程ID/课号" align="center" prop="courseId" width="100" />
<el-table-column label="学分" align="center" prop="credit" width="70">
<template slot-scope="scope">
<el-tag size="mini" type="primary">{{ scope.row.credit || 0 }}</el-tag>
</template>
</el-table-column>
<el-table-column label="教师姓名" align="center" prop="teacherName" width="100" />
<el-table-column label="班级ID" align="center" prop="classSection" width="80" />
<el-table-column label="学期" align="center" prop="semester" width="100" />
<el-table-column label="上课时间" align="center" width="180">
<template slot-scope="scope">
<div class="time-slots">
<div v-for="(slot, idx) in parseTimeSlots(scope.row.classTime)" :key="idx" class="time-slot-item">
<el-tag size="mini" :type="idx === 0 ? 'primary' : 'info'" effect="plain">
{{ slot }}
</el-tag>
</div>
</div>
</template>
</el-table-column>
<el-table-column label="容量" align="center" prop="capacity" width="70" />
<el-table-column label="已选" align="center" prop="selectedNum" width="70" />
<el-table-column label="剩余" align="center" prop="remainCapacity" width="70" />
<el-table-column label="我的状态" align="center" width="100">
<template slot-scope="scope">
<el-button
v-if="String(scope.row.isSelected) === '1'"
type="danger"
size="mini"
@click="handleUnselect(scope.row)"
>退选</el-button>
<el-button
v-else
type="primary"
size="mini"
:disabled="Number(scope.row.remainCapacity) <= 0"
@click="handleSelect(scope.row)"
>选择</el-button>
</template>
</el-table-column>
</el-table>

<pagination
v-show="total > 0"
:total="total"
:page.sync="queryParams.pageNum"
:limit.sync="queryParams.pageSize"
@pagination="getList"
/>
</div>
</template>

<script>
import { listSelection, selectSelection, unselectSelection, getMySchedule } from '@/api/system/selection'

export default {
name: 'StudentSelectionList',
data() {
return {
loading: false,
loadingSchedule: false,
total: 0,
selectionList: [],
mySchedule: [],
showSearch: true,
queryParams: {
pageNum: 1,
pageSize: 10,
courseName: undefined,
teacherName: undefined,
semester: undefined
}
}
},
computed: {
totalSelectedCredits() {
return this.mySchedule.reduce((sum, item) => sum + (Number(item.credit) || 0), 0)
}
},
created() {
this.getList()
this.loadMySchedule()
},
activated() {
// 页面被激活时（从其他标签页切换回来）刷新数据
this.getList()
this.loadMySchedule()
},
methods: {
getList() {
this.loading = true
listSelection(this.queryParams).then(response => {
this.selectionList = response.rows || []
this.total = response.total || 0
}).finally(() => {
this.loading = false
})
},
loadMySchedule() {
this.loadingSchedule = true
getMySchedule().then(response => {
this.mySchedule = response.rows || []
}).finally(() => {
this.loadingSchedule = false
})
},
handleSelect(row) {
const courseName = row && row.courseName ? row.courseName : ''
this.$modal.confirm(`是否确认选择课程"${courseName}"？`).then(() => {
return selectSelection(row.classId)
}).then((res) => {
if (res.code === 200) {
this.$modal.msgSuccess('选课成功')
this.getList()
this.loadMySchedule()
} else {
this.$modal.msgError(res.msg || '选课失败')
}
}).catch(() => {})
},
handleUnselect(row) {
const courseName = row && row.courseName ? row.courseName : ''
const selectionId = row.selectionId || row.id
if (!selectionId) {
	this.$modal.msgError('未找到选课记录ID，无法退课')
	return
}
this.$modal.confirm(`是否确认退选课程"${courseName}"？`).then(() => {
return unselectSelection(selectionId)
}).then(() => {
this.$modal.msgSuccess('退选成功')
this.getList()
this.loadMySchedule()
}).catch(() => {})
},
handleQuery() {
this.queryParams.pageNum = 1
this.getList()
},
resetQuery() {
this.queryParams.courseName = undefined
this.queryParams.teacherName = undefined
this.queryParams.semester = undefined
this.handleQuery()
},
parseTimeSlots(classTime) {
if (!classTime) return []
return classTime.split('|').map(s => s.trim()).filter(Boolean)
}
}
}
</script>

<style scoped>
.app-container {
padding: 20px;
}
.time-slots {
display: flex;
flex-direction: column;
align-items: center;
gap: 4px;
}
.time-slot-item {
line-height: 1.2;
}
.time-slot-item .el-tag {
white-space: nowrap;
}
</style>

<template>
	<div class="app-container">
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
			<el-table-column label="课程ID/课号" align="center" prop="courseId" />
			<el-table-column label="教师姓名" align="center" prop="teacherName" />
			<el-table-column label="教师工号" align="center" prop="teacherId" />
			<el-table-column label="学期" align="center" prop="semester" />
			<el-table-column label="上课时间" align="center" prop="classTime" />
			<el-table-column label="上课地点" align="center" prop="classPlace" />
			<el-table-column label="容量" align="center" prop="capacity" />
			<el-table-column label="已选" align="center" prop="selectedNum" />
			<el-table-column label="剩余" align="center" prop="remainCapacity" />
			<el-table-column label="我的状态" align="center">
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
import { listSelection, selectSelection, unselectSelection } from '@/api/system/selection'

export default {
	name: 'StudentSelectionList',
	data() {
		return {
			loading: false,
			total: 0,
			selectionList: [],
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
	created() {
		this.getList()
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
		handleSelect(row) {
			const courseName = row && row.courseName ? row.courseName : ''
			this.$modal.confirm(`是否确认选择课程“${courseName}”？`).then(() => {
				return selectSelection(row.id)
			}).then(() => {
				this.$modal.msgSuccess('选课成功')
				this.getList()
			}).catch(() => {})
		},
		handleUnselect(row) {
			const courseName = row && row.courseName ? row.courseName : ''
			this.$modal.confirm(`是否确认退选课程“${courseName}”？`).then(() => {
				return unselectSelection(row.id)
			}).then(() => {
				this.$modal.msgSuccess('退选成功')
				this.getList()
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
		}
	}
}
</script>

<style scoped>
.app-container {
	padding: 20px;
}
</style>

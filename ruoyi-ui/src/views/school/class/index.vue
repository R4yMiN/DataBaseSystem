<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="开课标识" prop="classId">
        <el-input
          v-model="queryParams.classId"
          placeholder="请输入开课标识"
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
      <el-form-item label="课号" prop="courseId">
        <el-input
          v-model="queryParams.courseId"
          placeholder="请输入课号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工号" prop="staffId">
        <el-input
          v-model="queryParams.staffId"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- 搜索/重置按钮 -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['school:class:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['school:class:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['school:class:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['school:class:export']"
        >导出</el-button>
      </el-col>

      <!-- 抽签按钮：保持在同一排 -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-magic-stick"
          size="mini"
          @click="handleRandomKick"
        >结束选课并随机抽签</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="classList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="开课标识" align="center" prop="classId" />
      <el-table-column label="学期" align="center" prop="semester" />
      <el-table-column label="课号" align="center" prop="courseId" />
      <el-table-column label="工号" align="center" prop="staffId" />
      <el-table-column label="上课时间" align="center" prop="classTime" />
      <el-table-column label="课程容量" align="center" prop="capacity" />
      <el-table-column label="已选人数" align="center" prop="selectedNum" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['school:class:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['school:class:remove']"
          >删除</el-button>
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

    <!-- 添加或修改开课对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学期" prop="semester">
          <el-input v-model="form.semester" placeholder="请输入学期" />
        </el-form-item>
        <el-form-item label="课号" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入课号" />
        </el-form-item>
        <el-form-item label="工号" prop="staffId">
          <el-input v-model="form.staffId" placeholder="请输入工号" />
        </el-form-item>
        <!-- 修改对话框中的表单内容 -->
        <el-form-item label="上课时间">
          <el-row :gutter="10">
            <el-col :span="8">
              <el-select v-model="form.dayOfWeek" placeholder="星期">
                <el-option label="周一" :value="1" />
                <el-option label="周二" :value="2" />
                <el-option label="周三" :value="3" />
                <el-option label="周四" :value="4" />
                <el-option label="周五" :value="5" />
              </el-select>
            </el-col>
            <el-col :span="8">
              <el-input-number v-model="form.lessonStart" :min="1" :max="12" placeholder="开始节" />
            </el-col>
            <el-col :span="8">
              <el-input-number v-model="form.lessonEnd" :min="1" :max="12" placeholder="结束节" />
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="上课周次">
          <el-input-number v-model="form.weekStart" :min="1" :max="20" /> 到
          <el-input-number v-model="form.weekEnd" :min="1" :max="20" /> 周
        </el-form-item>

        <el-form-item label="安排教室" prop="classroomId">
          <el-input v-model="form.classroomId" placeholder="请输入教室号（如101）" />
        </el-form-item>
        <el-form-item label="课程容量" prop="capacity">
          <el-input v-model="form.capacity" placeholder="请输入课程容量" />
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
// 确保 API 中导出了 executeRandomKick
import { listClass, getClass, delClass, addClass, updateClass, executeRandomKick } from "@/api/school/class"

export default {
  name: "Class",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      classList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        classId: null,
        semester: null,
        courseId: null,
        staffId: null,
        classTime: null,
        capacity: null,
        selectedNum: null
      },
      form: {},
      rules: {
        semester: [{ required: true, message: "学期不能为空", trigger: "blur" }],
        courseId: [{ required: true, message: "课号不能为空", trigger: "blur" }],
        staffId: [{ required: true, message: "工号不能为空", trigger: "blur" }],
        capacity: [{ required: true, message: "课程容量不能为空", trigger: "blur" }],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询开课列表 */
    getList() {
      this.loading = true
      listClass(this.queryParams).then(response => {
        this.classList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        classId: null,
        semester: null,
        courseId: null,
        staffId: null,
        classTime: null,
        capacity: null,
        selectedNum: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.classId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加开课"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const classId = row.classId || this.ids
      getClass(classId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改开课"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.classId != null) {
            updateClass(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addClass(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const classIds = row.classId || this.ids
      this.$modal.confirm('是否确认删除开课编号为"' + classIds + '"的数据项？').then(function() {
        return delClass(classIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 随机抽签按钮操作（新增） */
    handleRandomKick() {
      this.$modal.confirm('警告：此操作将永久剔除所有课程中超出容量的学生，建议在选课结束后执行。确认开始抽签吗？').then(function() {
        return executeRandomKick();
      }).then(() => {
        this.getList(); // 成功后刷新列表看人数变化
        this.$modal.msgSuccess("随机抽签完成，已清理超员课程");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('school/class/export', {
        ...this.queryParams
      }, `class_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

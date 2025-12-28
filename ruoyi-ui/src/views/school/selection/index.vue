<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学号" prop="studentId">
        <el-input
          v-model="queryParams.studentId"
          placeholder="请输入学号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开课ID" prop="classId">
        <el-input
          v-model="queryParams.classId"
          placeholder="请输入开课ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平时成绩" prop="normalScore">
        <el-input
          v-model="queryParams.normalScore"
          placeholder="请输入平时成绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考试成绩" prop="testScore">
        <el-input
          v-model="queryParams.testScore"
          placeholder="请输入考试成绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总评成绩" prop="totalScore">
        <el-input
          v-model="queryParams.totalScore"
          placeholder="请输入总评成绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['school:selection:add']"
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
          v-hasPermi="['school:selection:edit']"
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
          v-hasPermi="['school:selection:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['school:selection:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="selectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="选课记录ID" align="center" prop="selectionId" />
      <el-table-column label="学号" align="center" prop="studentId" />
      <el-table-column label="开课ID" align="center" prop="classId" />
      <el-table-column label="平时成绩" align="center" prop="normalScore" />
      <el-table-column label="考试成绩" align="center" prop="testScore" />
      <el-table-column label="总评成绩" align="center" prop="totalScore" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['school:selection:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['school:selection:remove']"
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

    <!-- 添加或修改选课成绩对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="开课ID" prop="classId">
          <el-input v-model="form.classId" placeholder="请输入开课ID" />
        </el-form-item>
        <el-form-item label="平时成绩" prop="normalScore">
          <el-input v-model="form.normalScore" placeholder="请输入平时成绩" />
        </el-form-item>
        <el-form-item label="考试成绩" prop="testScore">
          <el-input v-model="form.testScore" placeholder="请输入考试成绩" />
        </el-form-item>
        <el-form-item label="总评成绩" prop="totalScore">
          <el-input v-model="form.totalScore" placeholder="请输入总评成绩" />
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
import { listSelection, getSelection, delSelection, addSelection, updateSelection } from "@/api/school/selection"

export default {
  name: "Selection",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 选课成绩表格数据
      selectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentId: null,
        classId: null,
        normalScore: null,
        testScore: null,
        totalScore: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentId: [
          { required: true, message: "学号不能为空", trigger: "blur" }
        ],
        classId: [
          { required: true, message: "开课ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询选课成绩列表 */
    getList() {
      this.loading = true
      listSelection(this.queryParams).then(response => {
        this.selectionList = response.rows
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
        selectionId: null,
        studentId: null,
        classId: null,
        normalScore: null,
        testScore: null,
        totalScore: null
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
      this.ids = selection.map(item => item.selectionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加选课成绩"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const selectionId = row.selectionId || this.ids
      getSelection(selectionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改选课成绩"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.selectionId != null) {
            updateSelection(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSelection(this.form).then(response => {
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
      const selectionIds = row.selectionId || this.ids
      this.$modal.confirm('是否确认删除选课成绩编号为"' + selectionIds + '"的数据项？').then(function() {
        return delSelection(selectionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('school/selection/export', {
        ...this.queryParams
      }, `selection_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

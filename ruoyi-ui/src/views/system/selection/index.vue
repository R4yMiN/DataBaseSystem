<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学生学号" prop="studentId">
        <el-input
          v-model="queryParams.studentId"
          placeholder="请输入学生学号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input
          v-model="queryParams.studentName"
          placeholder="请输入学生姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程ID/课号" prop="courseId">
        <el-input
          v-model="queryParams.courseId"
          placeholder="请输入课程ID/课号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="教师工号" prop="teacherId">
        <el-input
          v-model="queryParams.teacherId"
          placeholder="请输入教师工号"
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
      <el-form-item label="上课时间" prop="classTime">
        <el-input
          v-model="queryParams.classTime"
          placeholder="请输入上课时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上课地点" prop="classPlace">
        <el-input
          v-model="queryParams.classPlace"
          placeholder="请输入上课地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程总容量" prop="capacity">
        <el-input
          v-model="queryParams.capacity"
          placeholder="请输入课程总容量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已选人数" prop="selectedNum">
        <el-input
          v-model="queryParams.selectedNum"
          placeholder="请输入已选人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="剩余容量" prop="remainCapacity">
        <el-input
          v-model="queryParams.remainCapacity"
          placeholder="请输入剩余容量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否被当前学生选中" prop="isSelected">
        <el-input
          v-model="queryParams.isSelected"
          placeholder="请输入是否被当前学生选中"
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
          v-hasPermi="['system:selection:add']"
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
          v-hasPermi="['system:selection:edit']"
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
          v-hasPermi="['system:selection:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:selection:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="selectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="" align="center" prop="id" />
      <el-table-column label="学生学号" align="center" prop="studentId" />
      <el-table-column label="学生姓名" align="center" prop="studentName" />
      <el-table-column label="课程ID/课号" align="center" prop="courseId" />
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="教师工号" align="center" prop="teacherId" />
      <el-table-column label="教师姓名" align="center" prop="teacherName" />
      <el-table-column label="学期" align="center" prop="semester" />
      <el-table-column label="上课时间" align="center" prop="classTime" />
      <el-table-column label="上课地点" align="center" prop="classPlace" />
      <el-table-column label="课程总容量" align="center" prop="capacity" />
      <el-table-column label="已选人数" align="center" prop="selectedNum" />
      <el-table-column label="剩余容量" align="center" prop="remainCapacity" />
      <el-table-column label="选课状态" align="center" prop="status" />
      <el-table-column label="是否被当前学生选中" align="center" prop="isSelected" />
      <el-table-column label="${comment}" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:selection:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:selection:remove']"
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

    <!-- 添加或修改学生自主选课对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学生学号" prop="studentId">
          <el-input v-model="form.studentId" placeholder="请输入学生学号" />
        </el-form-item>
        <el-form-item label="学生姓名" prop="studentName">
          <el-input v-model="form.studentName" placeholder="请输入学生姓名" />
        </el-form-item>
        <el-form-item label="课程ID/课号" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入课程ID/课号" />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="教师工号" prop="teacherId">
          <el-input v-model="form.teacherId" placeholder="请输入教师工号" />
        </el-form-item>
        <el-form-item label="教师姓名" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="请输入教师姓名" />
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="form.semester" placeholder="请输入学期" />
        </el-form-item>
        <el-form-item label="上课时间" prop="classTime">
          <el-input v-model="form.classTime" placeholder="请输入上课时间" />
        </el-form-item>
        <el-form-item label="上课地点" prop="classPlace">
          <el-input v-model="form.classPlace" placeholder="请输入上课地点" />
        </el-form-item>
        <el-form-item label="课程总容量" prop="capacity">
          <el-input v-model="form.capacity" placeholder="请输入课程总容量" />
        </el-form-item>
        <el-form-item label="已选人数" prop="selectedNum">
          <el-input v-model="form.selectedNum" placeholder="请输入已选人数" />
        </el-form-item>
        <el-form-item label="剩余容量" prop="remainCapacity">
          <el-input v-model="form.remainCapacity" placeholder="请输入剩余容量" />
        </el-form-item>
        <el-form-item label="是否被当前学生选中" prop="isSelected">
          <el-input v-model="form.isSelected" placeholder="请输入是否被当前学生选中" />
        </el-form-item>
        <el-form-item label="${comment}" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listSelection, getSelection, delSelection, addSelection, updateSelection } from "@/api/system/selection"

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
      // 学生自主选课表格数据
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
        studentName: null,
        courseId: null,
        courseName: null,
        teacherId: null,
        teacherName: null,
        semester: null,
        classTime: null,
        classPlace: null,
        capacity: null,
        selectedNum: null,
        remainCapacity: null,
        status: null,
        isSelected: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        studentId: [
          { required: true, message: "学生学号不能为空", trigger: "blur" }
        ],
        courseId: [
          { required: true, message: "课程ID/课号不能为空", trigger: "blur" }
        ],
        semester: [
          { required: true, message: "学期不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询学生自主选课列表 */
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
        id: null,
        studentId: null,
        studentName: null,
        courseId: null,
        courseName: null,
        teacherId: null,
        teacherName: null,
        semester: null,
        classTime: null,
        classPlace: null,
        capacity: null,
        selectedNum: null,
        remainCapacity: null,
        status: null,
        isSelected: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学生自主选课"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getSelection(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改学生自主选课"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除学生自主选课编号为"' + ids + '"的数据项？').then(function() {
        return delSelection(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/selection/export', {
        ...this.queryParams
      }, `selection_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

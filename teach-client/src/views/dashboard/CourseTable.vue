<template>
  <Navi/>
  <div class="app-container">
    <el-form :inline="true" :model="form" class="form-inline-query">
    <el-form-item label="课程编号" >
      <el-input v-model="form.num" placeholder="请输入课程编号" />
    </el-form-item>
    <el-form-item label="课程名">
      <el-input v-model="form.name" placeholder="请输入课程名"></el-input>

    </el-form-item>
      <el-form-item>
        <el-button class="commButton" size="mini" @click="doQuery()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button class="commButton" size="mini" @click="doAdd()">添加</el-button>
      </el-form-item>
    </el-form>    
    <el-table  class="table-content"
        :data="dataList"
        border
        style="width: 100%;"
        size="mini"
    >
    <el-table-column
        label="序号"
        fixed="left"
        width="50"          
        align="center"
        color="black"
        >
        <template v-slot="scope">
            {{ scope.$index+1 }}
        </template>
        </el-table-column><!--第一列-->
        <el-table-column
        label="课程编号"
        align="center"
        color="black"
        sortable
        prop="courseNum" /><!--courseNum-->
        <el-table-column
        label="课程名称"
        align="center"
        color="black"
        sortable
        prop="courseName" /><!--courseName-->
      <el-table-column
        label="学分"
        align="center"
        color="black"
        sortable
        prop="credit"/>
        <el-table-column
            label="操作"
            align="center"
            color="black"
        >

          <template v-slot="scope" >
            <div v-show="scope.row.editable==='true'">
              <el-button type="text" size="mini" @click="doEdit(scope.row.courseId)" >编辑</el-button>
              <el-button type="text" size="mini" @click="doDelete(scope.row.courseId)">删除</el-button>
              <el-button type="text" size="mini" @click="doInput(scope.row.courseId)">录入成绩</el-button>
            </div>
          </template>

        </el-table-column><!--编辑删除按钮-->
    </el-table>
    </div>
</template>
<script>
import {getCourseList,courseDelete} from '@/service/genServ.js'
import Navi from '@/components/Navi'
export default {
  name: 'CourseTable',
  components: {Navi
  },
  data() {
    return {
      form: {
        num:'',
        name:'',
      },
      dataList:[]
    }
  },
  created() {
    this.doQuery();
  },
  methods: {
    doQuery() {
       getCourseList({'num':this.form.num, 'name':this.form.name}).then(res => {
        this.dataList = res.data
      })
    },
    doEdit(courseId) {
        this.$router.push({ path: 'CourseEdit', query: { 'courseId':courseId }})
    },
    doInput(courseId){
      this.$router.push({path:'InputScore',query:{'courseId':courseId}})
    },
    doAdd() {
      this.$router.push({ path: 'CourseEdit'})
    },
    doDelete(courseId) {
      courseDelete({'courseId':courseId}).then(res => {
        console.log(res)
        this.$message({
          message:  '删除成功！',
          type: 'success',
        })
        this.doQuery();
      })
    },
  }
}
</script>

<style scoped>
</style>

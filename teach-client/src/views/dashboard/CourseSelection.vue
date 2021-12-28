<template>
  <Navi/>
  <div class="app-container">
    <el-form :inline="true" :model="form" class="form-inline-query" style="margin-top: 15px;">
      <el-form-item label="课程编号" >
        <el-input v-model="form.num" placeholder="请输入课程编号" />
      </el-form-item>
      <el-form-item label="课程名">
        <el-input v-model="form.name" placeholder="请输入课程名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="commButton" size="mini" @click="doQuery()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table  class="table-content"
               :data="dataList"
               border
               style="width: 100%;"
               size="mini"
               max-height="500"
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
          label="授课教师"
          align="center"
          color="black"
          sortable
          prop="teacherName" />
      <el-table-column
          label="学分"
          align="center"
          color="black"
          sortable
          prop="credit"/>
      <el-table-column
          label="开始选课日期"
          align="center"
          color="black"
          sortable
          prop="start"/>
      <el-table-column
          label="结束选课日期"
          align="center"
          color="black"
          sortable
          prop="end"/>
      <el-table-column
          label="上课时间"
          align="center"
          color="black"
          sortable
          prop="time"/>
      <el-table-column label="描述" align="center" color="black">
        <template v-slot="scope">
          <el-popover
              placement="left"
              width="200"
              trigger="click"
              :content=scope.row.description>
            <template v-slot:reference>
              <el-button >查看</el-button>
            </template>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column
          label="操作"
          align="center"
          color="black"
      >
        <template v-slot="scope">
          <el-button type="text" size="mini" @click="doSelect(scope.row.courseId)">选课</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <h3>已选课程：</h3>
  <div class="app-container">
    <el-table  class="table-content"
               :data="selected"
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
          label="授课教师"
          align="center"
          color="black"
          sortable
          prop="teacherName" />
      <el-table-column
          label="学分"
          align="center"
          color="black"
          sortable
          prop="credit"/>
      <el-table-column
          label="开始选课日期"
          align="center"
          color="black"
          sortable
          prop="start"/>
      <el-table-column
          label="结束选课日期"
          align="center"
          color="black"
          sortable
          prop="end"/>
      <el-table-column
          label="上课时间"
          align="center"
          color="black"
          sortable
          prop="time"/>

      <el-table-column label="描述" align="center" color="black">
        <template v-slot="scope">
          <el-popover
              placement="left"
              width="200"
              trigger="click"
              :content=scope.row.description>
            <template v-slot:reference>
              <el-button >查看</el-button>
            </template>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column
          label="操作"
          align="center"
          color="black"
      >
        <template v-slot="scope">
          <el-button type="text" size="mini" @click="doUnselect(scope.row.courseId)">退选</el-button>
        </template>
      </el-table-column><!--编辑删除按钮-->
    </el-table>
  </div>
</template>
<script>
import {getCourseList,getSelectedList,unselect,select} from '@/service/genServ.js'
import Navi from '@/components/Navi'
export default {
  name: 'CourseSelection',
  components: {Navi
  },
  data() {
    return {
      form: {
        num:'',
        name:'',
      },
      dataList:[],
      selected:[],
      dialogVisible: false
    }
  },
  created() {
    this.doQuery();
  },
  methods: {
    doQuery() {
      getCourseList({'num':this.form.num, 'name':this.form.name}).then(res => {
        this.dataList = res.data;
      })
      getSelectedList().then(res=>{
        this.selected=res.data;
      })
    },
    doUnselect(courseId){
      unselect({'courseId':courseId}).then(res=>{
        if(res.code==="0"){
          this.$message({
            message:'退选成功',
            type:'success'
          });
          this.doQuery();
        }else{
          this.$message({
            message:res.msg,
            type:'warning'
          })
        }
      })
    },
    doSelect(courseId){
      select({'courseId':courseId}).then(res=>{
        if(res.code==="0"){
          this.$message({
            message:'选课成功',
            type:'success'
          });
          this.doQuery();
        }else{
          this.$message({
            message:res.msg,
            type:'warning'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
</style>

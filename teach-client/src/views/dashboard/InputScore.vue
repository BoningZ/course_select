<template>
  <Navi/>
  <div class="app-container">
    <el-button class="commButton" size="mini" @click="doSubmit(0)">暂存</el-button>
    <el-button class="commButton" size="mini" @click="doSubmit(1)">提交</el-button>
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
          label="学号"
          align="center"
          color="black"
          prop="sid" /><!--courseNum-->
      <el-table-column
          label="姓名"
          align="center"
          color="black"
          prop="name" /><!--courseName-->
      <el-table-column
          label="有效性"
          align="center"
          color="black"
          prop="valid" />
      <el-table-column label="成绩（百分制）" prop="score">
      <template v-slot="scope">
        <el-input v-model="scope.row.score" size="small"  style="width:240px"></el-input>
      </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import {getStudentList,submitScore} from '@/service/genServ.js'
import Navi from '@/components/Navi'
export default {
  name: 'InputScore',
  components: {Navi
  },
  data() {
    return {
      courseId:null,
      dataList:[]
    }
  },
  created() {
    this.courseId = this.$route.query.courseId;
    this.doQuery();
  },
  methods: {
    doQuery() {
      getStudentList({'courseId':this.courseId}).then(res => {
        this.dataList = res.data
      })
    },
    doSubmit(valid){
      submitScore({'score':this.dataList,'valid':valid}).then(res=>{
        if(res.code==='0'){
          this.$message({
            message: '成功！',
            type: 'success',
          })
        }else{
          this.$message({
            message:res.msg,
            type:'warning',
          })
        }
      })
    },
  }
}
</script>

<style scoped>
</style>

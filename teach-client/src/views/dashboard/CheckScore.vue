<template>
  <Navi/>
  <div class="app-container">
    五分制绩点：{{this.GPA}}
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
          label="课程号"
          align="center"
          color="black"
          prop="courseNum" /><!--courseNum-->
      <el-table-column
          label="课程名"
          align="center"
          color="black"
          prop="courseName" /><!--courseName-->
      <el-table-column
          label="学分"
          align="center"
          color="black"
          prop="credit" />
      <el-table-column
          label="分数"
          align="center"
          color="black"
          prop="score" />
      <el-table-column
          label="班级排名"
          align="center"
          color="black"
          prop="rankInClass" />
      <el-table-column
          label="总排名"
          align="center"
          color="black"
          prop="rank" />
    </el-table>
  </div>
</template>
<script>
import {getScoreList,getGPA} from '@/service/genServ.js'
import Navi from '@/components/Navi'
export default {
  name: 'CheckScore',
  components: {Navi
  },
  data() {
    return {
      GPA:null,
      dataList:[]
    }
  },
  created() {
    this.doQuery();
  },
  methods: {
    doQuery() {
      getScoreList().then(res => {
        this.dataList = res.data;
      });
      getGPA().then(res=>{
        this.GPA=res.data;
      })
    },
  }
}
</script>

<style scoped>
</style>

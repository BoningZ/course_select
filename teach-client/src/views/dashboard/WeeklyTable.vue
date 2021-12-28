<template>
  <Navi/>
  <div class="app-container">
    <el-table  class="table-content"
               :data="weekly"
               border
               style="width: 100%;"
               size="mini"
    >
      <el-table-column
          label="节次"
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
          label="周一"
          align="center"
          color="black"
          prop="1" /><!--courseNum-->
      <el-table-column
          label="周二"
          align="center"
          color="black"
          prop="2" /><!--courseName-->
      <el-table-column
          label="周三"
          align="center"
          color="black"
          prop="3"/>
      <el-table-column
          label="周四"
          align="center"
          color="black"
          prop="4"/>
      <el-table-column
          label="周五"
          align="center"
          color="black"
          prop="5"/>
    </el-table>
  </div>
</template>

<script>

import {getWeeklyTable} from "@/service/genServ";
import Navi from '@/components/Navi'
export default {
  name: "WeeklyTable",
  components:{ Navi},
  data(){
    return{
      weekly:[]
    }
  },
  created() {
    this.doQuery();
  },
  methods:{
    doQuery() {
      getWeeklyTable().then(res => {
        this.weekly = res.data;
      })
    }
  }
}
</script>

<style scoped>

</style>
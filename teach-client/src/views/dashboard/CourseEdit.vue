<template>
  <Navi/>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="30%" class="form-div">
      <el-form-item label="课程号">
        <el-input v-model="form.courseNum" placeholder="请输入课程号" />
      </el-form-item>
      <el-form-item label="课程名">
        <el-input  v-model="form.courseName" placeholder="请输入课程名"  />
      </el-form-item>
      <el-form-item label="学分">
        <el-input  v-model="form.credit" type="number" placeholder="请输入学分"  />
      </el-form-item>
      <el-form-item label="描述">
        <el-input  v-model="form.description" placeholder="请输入描述"  />
      </el-form-item>
      <el-form-item label="上课时间">
      <br><label>周一：</label>
      <input type="checkbox" id="1-1" value="1" v-model="time">
      <label for="1-1">第一节</label>
      <input type="checkbox" id="1-2" value="2" v-model="time">
      <label for="1-2">第二节</label>
      <input type="checkbox" id="1-3" value="3" v-model="time">
      <label for="1-3">第三节</label>
      <input type="checkbox" id="1-4" value="4" v-model="time">
      <label for="1-4">第四节</label>
      <input type="checkbox" id="1-5" value="5" v-model="time">
      <label for="1-5">第五节</label>
      <br><label>周二：</label>
      <input type="checkbox" id="2-1" value="6" v-model="time">
      <label for="2-1">第一节</label>
      <input type="checkbox" id="2-2" value="7" v-model="time">
      <label for="2-2">第二节</label>
      <input type="checkbox" id="2-3" value="8" v-model="time">
      <label for="2-3">第三节</label>
      <input type="checkbox" id="2-4" value="9" v-model="time">
      <label for="2-4">第四节</label>
      <input type="checkbox" id="2-5" value="10" v-model="time">
      <label for="2-5">第五节</label>
      <br><label>周三：</label>
      <input type="checkbox" id="3-1" value="11" v-model="time">
      <label for="3-1">第一节</label>
      <input type="checkbox" id="3-2" value="12" v-model="time">
      <label for="3-2">第二节</label>
      <input type="checkbox" id="3-3" value="13" v-model="time">
      <label for="3-3">第三节</label>
      <input type="checkbox" id="3-4" value="14" v-model="time">
      <label for="3-4">第四节</label>
      <input type="checkbox" id="3-5" value="15" v-model="time">
      <label for="3-5">第五节</label>
        <br><label>周四：</label>
        <input type="checkbox" id="4-1" value="16" v-model="time">
        <label for="4-1">第一节</label>
        <input type="checkbox" id="4-2" value="17" v-model="time">
        <label for="4-2">第二节</label>
        <input type="checkbox" id="4-3" value="18" v-model="time">
        <label for="4-3">第三节</label>
        <input type="checkbox" id="4-4" value="19" v-model="time">
        <label for="4-4">第四节</label>
        <input type="checkbox" id="4-5" value="20" v-model="time">
        <label for="4-5">第五节</label>
        <br><label>周五：</label>
        <input type="checkbox" id="5-1" value="21" v-model="time">
        <label for="5-1">第一节</label>
        <input type="checkbox" id="5-2" value="22" v-model="time">
        <label for="5-2">第二节</label>
        <input type="checkbox" id="5-3" value="23" v-model="time">
        <label for="5-3">第三节</label>
        <input type="checkbox" id="5-4" value="24" v-model="time">
        <label for="5-4">第四节</label>
        <input type="checkbox" id="5-5" value="25" v-model="time">
        <label for="5-5">第五节</label>
      </el-form-item>

<!--      <el-form-item label="开始选课日期">-->
<!--        <el-date-picker v-model="form.start" type="date" placeholder="选择开始日期" value-format="YYYY-MM-DD"></el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="结束选课日期">-->
<!--        <el-date-picker v-model="form.end" type="date" placeholder="选择结束日期" value-format="YYYY-MM-DD"></el-date-picker>-->
<!--      </el-form-item>-->

      <el-form-item label="选课日期">
        <el-date-picker
            v-model="form.daterange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button class="editButton" size="mini" @click="submit">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {
  getCourseInfo,
  courseSubmit,
} from "@/service/genServ.js";
import Navi from '@/components/Navi'
export default {
  name: "CourseEdit",
  components: {Navi},
  data() {
    return {
      form: {
        courseId: null,
        teacherId:null,
        courseNum: "",
        courseName: "",
        description:"" ,
        start:"",
        end:"",
        daterange:[],
        credit:null,
      },
      time:[],
    };
  },
  created() {
    this.form.courseId = this.$route.query.courseId;
    this.doQuery();
  },
  methods: {
    doQuery() {
      getCourseInfo({ courseId: this.form.courseId }).then(
          (res) => {
            if(res.code==='0') {
              this.form = res.data.form;
              this.time=res.data.time;
              this.form.daterange=[];
              this.form.daterange[0]=this.form.start;
              this.form.daterange[1]=this.form.end;
            }else{
              this.$message({
                message:res.msg,
                type:'warning'
              })

            }
          }
      );
    },
    submit() {
      if(this.form.credit<0){
        this.$message({
          message:"学分需为正数",
          type:'warning'
        })
      }else {
        this.form.start = this.form.daterange[0];
        this.form.end = this.form.daterange[1];
        courseSubmit({
          form: this.form,
          time: this.time,
        }).then((res) => {
          if (res.code === '0') {
            this.$message({
              message: '提交成功！',
              type: 'success',
            })
            this.$router.go(-1);
          } else {
            this.$message({
              message:res.msg,
              type:'warning'
            })
          }
        });
      }
    },
  },
};
</script>

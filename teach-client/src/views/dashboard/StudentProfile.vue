<template>
  <Navi/>
  <div  class="form-div">
    <el-form ref="editForm" :model="editForm" :rules="editRules" label-position="left" label-width="50px" status-icon style="margin-top: 15px;">
      <br/>
      <el-row align="middle">
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="姓名" >
            <el-input v-model="name" maxlength="20" show-word-limit style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="学号" >
            <el-input v-model="sid" :disabled="true"  maxlength="20"  style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="地址" >
            <el-input type="textarea" :rows="2" v-model="addr" maxlength="70" show-word-limit  style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="邮箱" >
            <el-input v-model="email"   style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="生日" >
            <el-date-picker v-model="birthday" type="date" placeholder="选择生日" value-format="YYYY-MM-DD" style="width: 500px;background-color: #f4f4f5"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="电话" >
            <el-input v-model="tel"   style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="班级" >
            <el-select v-model="this.class" filterable placeholder="请选择" style="width: 500px;background-color: #f4f4f5">
              <el-option
                  v-for="item in classes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="年级" >
            <el-input v-model="grade"  type="number" style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="专业" >
            <el-input v-model="major"   style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row >
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item align="center">
            <el-button type="primary" @click="submit">提交</el-button>
          </el-form-item>
        </el-col>
      </el-row>


    </el-form>
  </div>
</template>

<script>
import {getProfile,submitProfile} from "@/service/genServ";
import Navi from '@/components/Navi'
export default {
  name: "StudentProfile",
  components:{Navi},
  data(){
    return{
      sid:"",
      name:"",
      addr:"",
      email:"",
      birthday:"",
      tel:"",
      class:null,
      grade:null,
      major:"",
      classes:[],
    }
  },
  created() {
    this.doQuery();
  },
  methods:{
    doQuery(){
      getProfile().then(res=>{
        this.sid=res.data.sid;
        this.name=res.data.name;
        this.addr=res.data.addr;
        this.email=res.data.email;
        this.birthday=res.data.birthday;
        this.tel=res.data.tel;
        this.class=res.data.class;
        this.grade=res.data.grade;
        this.major=res.data.major;
        this.classes=res.data.classes;
      })
    },
    submit(){
      if(!this.isEmail(this.email)){
        this.$message({
          message:  '邮件格式不正确',
          type: 'warning',
        })
      }else if(!this.isTel(this.tel)){
        this.$message({
          message:  '电话格式不正确（中国大陆手机号码，不带区号）',
          type: 'warning',
        })
      }else if(this.grade<=0||this.grade>10){
        this.$message({
          message:  '年级应为1~10的整数！',
          type: 'warning',
        })
      }else submitProfile({"sid":this.sid,
                          "name":this.name,
                          "addr":this.addr,
                          "email":this.email,
                          "birthday":this.birthday,
                          "tel":this.tel,
                          "class":this.class,
                          "grade":this.grade,
                          "major":this.major}).then(res=>{
        if(res.code==='0'){
          this.$message({
            message:  '成功',
            type: 'success',
          })
        }else{
          this.$message({
            message:  res.msg,
            type: 'warning',
          })
        }
      })
    },
    isEmail(str){
      var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
      return reg.test(str);
    },
    isTel(str){
      return /^1[3456789]\d{9}$/.test(str);
    },

  }
}
</script>


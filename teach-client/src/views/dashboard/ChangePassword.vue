<template>
  <Navi/>

  <div  class="form-div">
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-position="left" label-width="100px" status-icon style="margin-top: 15px;">
        <br/>
        <el-row align="middle">
          <el-col :span="4">
            &nbsp;
          </el-col>
          <el-col :span="10">
            <el-form-item label="旧密码" prop="oldPas">
              <el-input v-model="oldPassword" :type="passw" style="width: 200px;background-color: #f4f4f5"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            &nbsp;
          </el-col>
          <el-col :span="10">
            <el-form-item label="新密码" prop="oldPas">
              <el-input v-model="newPassword" :type="passw"  style="width: 200px;background-color: #f4f4f5"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            &nbsp;
          </el-col>
          <el-col :span="10">
            <el-form-item label="确认密码" prop="oldPas">
              <el-input v-model="newPassword1" :type="passw"  style="width: 200px;background-color: #f4f4f5"/>
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
import {changePassword} from '@/service/genServ.js'
import Navi from '@/components/Navi'
export default {
  name: 'ChangePassword',
  components: {Navi
  },
  data() {
    return {
      passw: 'password',
      oldPassword: '',
      newPassword: '',
      newPassword1: '',
    }
  },
  methods: {
    submit() {
      if(!this.newPassword){
        this.$message({
          message:  '请填入新密码',
          type: 'warning',
        })
      }else if (this.newPassword !== this.newPassword1) {
        this.$message({
          message:  '口令不一致',
          type: 'warning',
        })
      } else {
        changePassword({ 'oldPassword': this.oldPassword, 'newPassword': this.newPassword }).then(response => {
          if (response.code === '0') {
            this.$message({
              message:  '成功',
              type: 'success',
            })
            this.$router.go(-1)
          } else {
            this.$message({
              message:  response.msg,
              type: 'warning',
            })
          }
        })
      }
    }
  }
}
</script>
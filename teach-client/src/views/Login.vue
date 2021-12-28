<template>
  <body id="poster">
    <el-form class="login-container" label-position="left" label-width="0px">
    <div class="login">
        <h2>登录</h2>
        <form @submit.prevent="handleSubmit">
            <div class="form-group">
                  <el-label for="username">用户名 </el-label>
                 <el-input style="width: 75%" clearable v-model="username"  name="username" placeholder="请输入用户名"></el-input>
            </div>
            <div class="form-group">
              <h2></h2>
              <label htmlFor="password">密码  </label>&nbsp;&nbsp;
              <el-input style="width: 75%"  clearable placeholder="请输入密码" v-model="password" show-password></el-input>
            </div>
          <h2></h2>
            <div class="form-group">
                <el-button class="btn btn-primary" type="primary" @click='handleSubmit()' style="width: 50%;background: #409EFF;border: none">登录</el-button>
            </div>
        </form>
    </div>
    <div>
      <h2></h2>
      <el-button @click='reg()' style="width: 50%;">注册</el-button>
    </div>
    </el-form>
  </body>
</template>


<script>
export default {
  name: 'Login',
   data () {
        return {
            username: '',
            password: '',
        }
    },
  methods: {
    handleSubmit() {
        const { username, password } = this;
        if (username && password) {
            this.$store.dispatch('login', {
                username : username,
                password : password
            })
        }
    },
    reg(){
      this.$router.push('/Register');
    }
  }
}
</script>

<style>
#poster {
  background:url("../assets/login.jpg") no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}
body{
  margin: 0px;
}
.login-container {
  border-radius: 30px;
  background-clip: padding-box;
  margin: 150px auto;
  width: 400px;
  padding: 35px 35px 50px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}

</style>
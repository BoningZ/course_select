import 
<template>
  <Navi/>
  <div class="home">
<!--    <input type="button" value="登出" v-on:click="logout()">-->
    <el-button type="danger" @click='logout'>登出</el-button>
  </div>
</template>

<script>
import Navi from '@/components/Navi'
import router from '@/router/index.js'

export default {
  name: 'Home',
  components:{ Navi},
  methods:{
    logout() {
      this.$store.commit('logout')
      router.push('/login')
    }
  }
}
</script>

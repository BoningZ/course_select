<template>
  <div class="navi">
    <el-menu
      class="el-menu-demo"
      mode="horizontal"
      router
      >
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item class="menu-item" v-for="(v,i) in list" :key="i" :index="'/'+v.name">{{v.title}}</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import {getMenuList} from '@/service/genServ.js'
export default {
  name: 'Navi',
  components: {
  },
  created() {
    this.setMenu()
  },
  data() {
      return {
        list:[]
      }
    },
  methods:{
      setMenu() {
        var self = this
        getMenuList()
          .then(data => {
            self.list = data.data
          }
          )
      }
    }
}
</script>

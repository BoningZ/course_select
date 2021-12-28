<template>
  <Navi/>
  <div class="sample">
    <img alt="Vue logo" src="@/assets/logo.png">
    <p>This is first page</p>
    <p>username:{{ username }}</p>
    <p>jwtToken:{{ jwtToken }}</p>
    <p>logged:{{loggedIn}}</p>
    <p>response:{{ response }}</p>
    <input type="button" value="Test" v-on:click="test()">
    <input type="button" value="Logout" v-on:click="logout()">
  </div>
</template>

<script>
import Navi from '@/components/Navi'
import {samplePost} from '@/service/genServ.js'

import router from '@/router/index.js'
import { mapState } from 'vuex'

export default {
  name: 'Sample',
  components: {
      Navi
  },
  data() {
    return {
    response : ""
    }
  },
  computed: {

    ...mapState([
      'username', 'jwtToken'
    ])
  },
  methods: {
    logout() {
      this.$store.commit('logout')
      router.push('/login')
    },
    test(){
      samplePost()
      .then(res => {
        this.response = res
      })
    }
  }
}
</script>

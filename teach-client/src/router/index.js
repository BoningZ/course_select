import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login'

const routes = [
  {
    path: '/',
    name: 'Home',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/views/Home.vue')
  },

  {
    path: '/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: Login
  },

  {
    path: '/DashSample',
    name: 'DashSample',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/views/dashboard/DashSample.vue')
  },

  {
    path: '/ChangePassword',
    name: 'ChangePassword',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/views/dashboard/ChangePassword.vue')
  },

  {
    path: '/CourseTable',
    name: 'CourseTable',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/views/dashboard/CourseTable.vue')
  },

  {
    path: '/CourseEdit',
    name: 'CourseEdit',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/views/dashboard/CourseEdit.vue')
  },

  {
    path:'/Register',
    name:'Register',
    component:() => import('@/views/Register.vue')
  },

  {
    path:'/TeacherProfile',
    name:'TeacherProfile',
    component:() => import('@/views/dashboard/TeacherProfile.vue')
  },
  {
    path:'/StudentProfile',
    name:'StudentProfile',
    component:()=>import('@/views/dashboard/StudentProfile.vue')
  },
  {
    path:'/CourseSelection',
    name:'CourseSelection',
    component:()=>import('@/views/dashboard/CourseSelection.vue')
  },
  {
    path: '/WeeklyTable',
    name:'WeeklyTable',
    component:()=>import('@/views/dashboard/WeeklyTable.vue')
  },
  {
    path:'/InputScore',
    name:'InputScore',
    component:()=>import('@/views/dashboard/InputScore.vue')
  },
  {
    path:'/CheckScore',
    name:'CheckScore',
    component:()=>import('@/views/dashboard/CheckScore.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

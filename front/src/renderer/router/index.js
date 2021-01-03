import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '../views/layout/Layout'
import Layout1 from '../views/layout/Layout1'

export const constantRouterMap = [{
  path: '/login',
  component: () =>
    import('@/views/login/index'),
  hidden: true
},
{
  path: '/404',
  component: () =>
    import('@/views/404'),
  hidden: true
},
{
  path: '/',
  component: Layout,
  redirect: '/dashboard',
  name: 'Dashboard',
  hidden: true,
  meta: {
    index: 0
  },
  children: [{
    path: 'dashboard',
    component: () =>
      import('@/views/dashboard/index')
  }]
},

{
  name: 'Study',
  path: '/Study',
  component: Layout,
  redirect: '../',
  meta: { title: '自主学习', icon: 'classMessage', role: 'all', index: 1 },
  children: [
    {
      path: 'data',
      name: 'Data',
      component: () =>
        import('@/views/AutonomousLearning/data'),

      meta: { title: '学习资料', icon: 'classInfo', role: 'student', index: 2 }
    },
    {
      path: 'analysis',
      name: 'Analysis',
      component: () =>
        import('@/views/AutonomousLearning/analysis'),

      meta: { title: '成绩分析', icon: 'classInfo', role: 'student', index: 3 }
    },
    {
      path: 'test/:id',
      name: 'Test',
      component: () =>
        import('@/views/AutonomousLearning/test'),

      meta: { title: '课前小练习', icon: 'classInfo', role: 'student', index: 4 },
      hidden: true
    },
    {
      path: 'info',
      name: '添加课程',
      component: () => import('@/views/edu/course/info'),
      meta: { title: '添加课程', icon: 'tree', role: 'teacher', index: 5 }
    },
    {
      path: 'info/:id',
      name: 'EduCourseInfoEdit',
      component: () => import('@/views/edu/course/info'),
      meta: { title: '编辑课程基本信息', noCache: true, role: 'teacher', index: 6 },
      hidden: true
    },

    {
      path: 'player/:id',
      name: 'player',
      component: () => import('@/views/player/index'),
      meta: { title: '播放器', noCache: true, role: 'student', index: 7 },
      hidden: true
    },
    {
      path: 'chapter/:id',
      name: 'EduCourseChapterEdit',
      component: () => import('@/views/edu/course/chapter'),
      meta: { title: '编辑课程大纲', noCache: true, role: 'teacher', index: 8 },
      hidden: true
    },
    {
      path: 'publish/:id',
      name: 'EduCoursePublishEdit',
      component: () => import('@/views/edu/course/publish'),
      meta: { title: '发布课程', noCache: true, role: 'teacher', index: 9 },
      hidden: true
    }
  ]
},
/**
   * 课堂模块----学生和老师的演示入口
   */
{
  path: '/display',
  component: Layout,
  redirect: '/display/teacher',
  name: 'Display',
  meta: { title: '课前指导', icon: 'class', role: 'all', index: 10 },
  children: [
    {
      path: 'room',
      name: 'Room',
      component: () =>
        import('@/views/chat/ChatRoom'),

      meta: { title: '课前讨论', icon: 'classInfo', role: 'all', index: 11 }
    },
    {
      path: 'student',
      name: 'Student',
      component: () =>
        import('@/views/display/student'),
      meta: { title: '观看演示', icon: 'watch', role: 'student', index: 12 }
    },
    {
      path: 'student_startExp',
      name: 'Student_startExp',
      component: () =>
        import('@/views/display/student_startExp'),
      meta: { title: '开始实验', icon: 'watch', role: 'student', index: 13 }
    },
    {
      path: 'test',
      name: 'Test',
      component: () =>
        import('@/views/display/test'),
      meta: { title: '签到详情', icon: 'watch', role: 'all', index: 14 }
    },
    {
      path: 'addstudent',
      name: 'Addstudent',
      component: () =>
        import('@/views/addstudent/index'),
      meta: { title: '导入名单', icon: 'watch', role: 'teacher', index: 15 }
    },
    {
      path: 'upload',
      name: 'Upload',
      component: () =>
        import('@/views/display/upload'),
      meta: { title: '预习资料', icon: 'watch', role: 'teacher', index: 16 }
    },
    {
      path: 'download',
      name: 'Download',
      component: () =>
        import('@/views/display/download'),
      meta: { title: '预习资料', icon: 'watch', role: 'student', index: 17 }
    }
  ]
},
/**
   * 课堂聊天室模块
   */

{
  name: 'Chat',
  path: '/chat',
  component: Layout,
  redirect: '../',
  meta: { title: '课堂教学', icon: 'classMessage', role: 'all', index: 18 },
  children: [
    
      
        {
          path: 'student_startExp',
          name: 'Student_startExp',
          component: () =>
            import('@/views/display/student_startExp'),
          meta: { title: '课堂讲解', icon: 'watch', role: 'teacher' }
        },
        {
          path: 'teacher',
          name: 'Teacher',
          component: () =>
            import('@/views/display/teacher'),
          meta: { title: '实验演示', icon: 'exeperimentDemo', role: 'teacher', index: 20 }

        },
  
        // {
        //   path: 'notice',
        //   name: 'Notice',
        //   component: () =>
        //     import('@/views/chat/Notice'),
        //   meta: { title: '课堂通知', icon: 'classInformation', role: 'all', index: 22 }
        // },
        // {
        //   path: 'release',
        //   name: 'Release',
        //   component: () =>
        //     import('@/views/chat/Release'),
        //   meta: { title: '发布通知', icon: 'sendInfo', role: 'teacher', index: 23 }
        // },
        {
          path: 'info',
          name: 'Info',
          component: () =>
            import('@/views/monitor/Info'),
          meta: { title: '教学监控', icon: 'monitor', role: 'teacher', index: 24 }
        }

    

  ]
},
{
  name: 'Result',
  path: '/result',
  component: Layout,
  redirect: 'noredirect',

  meta: { title: '课后指导', icon: 'exeperimentResource', role: 'all', index: 25 },
  children: [
    {
      path: 'allHistory',
      name: 'AllHistory',
      component: () =>
        import('@/views/evaluate/AllHistory'),
      meta: { title: '成绩查看', icon: 'example', role: 'teacher', index: 26 }
    },
    {
      path: 'allHistory2',
      name: 'AllHistory2',
      component: () =>
        import('@/views/evaluate/AllHistory2'),
      meta: { title: '成绩查看', icon: 'example', role: 'student', index: 27 }
    },
    {
      path: 'my',
      name: 'My',
      component: () =>
        import('@/views/evaluate/MyEvaluate'),
      meta: { title: '实验评价', icon: 'example', role: 'student', index: 28 }
    },
    {
      path: 'my2',
      name: 'My2',
      component: () =>
        import('@/views/evaluate/MyEvaluate2'),
      meta: { title: '学生反馈', icon: 'example', role: 'teacher', index: 29 }
    },
    {
      name: 'Result2',
      path: '/result2',
      component: Layout1,
      redirect: 'noredirect',
      // alwaysShow: true,

      meta: { title: '实验分析', icon: 'exeperimentResource', role: 'teacher', index: 30 },
      children: [
        {
          path: 'result_catagory',
          name: 'Result_catagory',
          component: () =>
            import('@/views/evaluate/Result_catagory'),
          meta: { title: '分类统计', icon: 'example', role: 'teacher', index: 31 }
        },
        {
          path: 'result_history',
          name: 'Result_history',
          component: () =>
            import('@/views/evaluate/Result_history'),
          meta: { title: '历史成绩', icon: 'example', role: 'teacher', index: 32 }
        },
        {
          path: 'result_compare',
          name: 'Result_compare',
          component: () =>
            import('@/views/evaluate/Result_compare'),
          meta: { title: '同类实验', icon: 'example', role: 'teacher', index: 33 }
        }
      ]
    }
  ]
},

// {
//   name: 'Afterclass',
//   path: '/afterclass',
//   component: Layout,
//   redirect: 'noredirect',

//   meta: { title: '课后活动', icon: 'exeperimentResource', role: 'all', index: 34 },
//   children: [
//     {
//       path: 'homework',
//       name: 'Homework',
//       component: () =>
//         import('@/views/afterclass/homework'),
//       meta: { title: '课后作业', icon: 'example', role: 'all', index: 35 }
//     },
//     {
//       path: 'afterschool_test',
//       name: 'Afterschool_test',
//       component: () =>
//         import('@/views/afterclass/afterschool_test'),
//       meta: { title: '课后测试', icon: 'example', role: 'all', index: 36 }
//     },
//     {
//       path: 'afterschool_talk',
//       name: 'Afterschool_talk',
//       component: () =>
//         import('@/views/afterclass/afterschool_talk'),
//       meta: { title: '课后讨论', icon: 'example', role: 'all', index: 37 }

//     },
//     {
//       path: 'agora',
//       name: 'agora',
//       component: () =>
//         import('@/views/afterclass/agora'),
//       meta: { title: 'agora', icon: 'example', role: 'all', index: 38 }

//     }
//   ]
// },
{ path: '*', redirect: '/404', hidden: true }
]
export default new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

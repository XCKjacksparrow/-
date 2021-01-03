import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import App from './App'
import router from './router'
import store from './store'
import adapter from 'webrtc-adapter'
import ip from 'ip'
import '@/icons' // icon
import '@/permission' // permission control
import MyWebScoket from '@/utils/webSocket'
import MyWebRTC from '@/utils/webRTC'
// 引入echarts
import echarts from 'echarts'
// 引入animated-vue
import AnimatedVue from 'animated-vue'
import 'animate.css/animate.css'

Vue.use(AnimatedVue)

Vue.prototype.$echarts = echarts
Vue.prototype.serverIp = '192.168.2.18' // websocket服务端地址127.0.0.1
Vue.prototype.localIp = ip.address() // '192.168.10.50' //'192.168.217.1' // ip.address()// 教师端ip地址(学生端不用改)
Vue.prototype.serverPort = '8080'

Vue.prototype.downloadFilePath = 'D:\\expTemp' // 下载的文件路径
Vue.prototype.imgeSendTime = 6 // 演示图片发送频率，默认三秒
// WEBRTC

// let rtc = new MyWebRTC(socket,'192.168.56.1','student')
// rtc.doConnection()
// rtc.sendOffer()
// store.commit('PUSH_RTCPOOL',rtc)

if (!process.env.IS_WEB) Vue.use(require('vue-electron'))

Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

new Vue({
  components: { App },
  router,
  store,
  template: '<App/>'
}).$mount('#app')

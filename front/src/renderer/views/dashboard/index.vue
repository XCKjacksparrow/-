<template>
  <div class="dashbord">
    <div class="dashbord-left">
      <el-card>
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="课程信息 Course Information" name="1">
            <el-button
              type="primary"
              @click="drawer = true"
              style="margin: 20px 0 5px 70px"
              >显示签到详情</el-button
            >

          </el-collapse-item>
          <el-collapse-item title="当前在线教师 Teacher" name="2">
            <el-table :data="TeacherData" stripe style="width: 100%">
              <el-table-column type="index" width="50"> </el-table-column>
              <el-table-column prop="userName" label="姓名" width="180">
              </el-table-column>
              <el-table-column prop="role" label="身份" width="180">
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="当前在线学生 Student" name="3">
            <el-table :data="StudentData" stripe style="width: 100%">
              <el-table-column type="index" width="50"> </el-table-column>
              <el-table-column prop="userName" label="姓名" width="180">
              </el-table-column>
              <el-table-column prop="role" label="身份" width="180">
              </el-table-column>
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
    <el-drawer title="签到表" :visible.sync="drawer" :with-header="false">
      <div style="width: 600px; height: 100%">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="name" label="姓名" width="180">
          </el-table-column>
          <el-table-column prop="isSign" label="是否签到" width="180">
          </el-table-column>
          <el-table-column prop="signInTime" label="签到时间" width="180">
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { getUserMedia } from '../../utils'
import { getResourceList } from '../../api/signIn'


import { getChatList, addChat } from '../../api/chat'

const img = require('../../assets/下线.jpg')
import { getPeoples } from '../../api/dashboard'
import { Message } from 'element-ui'
import { mapGetters } from 'vuex'
import MyWebSocket from '../../utils/webSocket'
import MyWebRTC from '../../utils/webRTC'
import moment from 'moment'
import Vue from 'vue'
import { getuserlist, getuserlistById ,getonlineuserlist ,getonlineteacherlist} from '../../api/user'

import AgoraRtcEngine from 'agora-electron-sdk'
import path from 'path'
import os from 'os'
const shell = require('electron').shell
const APPID = process.env['AGORA_APPID'] || 'a0bec9a017144787a4976b9102ff9667'
const echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/pie')
require('echarts-gl')

export default {
  data() {
    return {
      noneVoice: true,
      Historys: [],
      message: '',
      tableData: [],
      drawer: false,
      opendrawer: false,
      activeNames: ['1'],
      StudentData: [
        
  
      ],
      TeacherData: []
    }
  },
  created() {},
  mounted() {
    this.initMain()
    this.loadData()
    this.bindSocketMessage()
    this.receiveResource()
    this.loadUserList()
  },
  methods: {

    loadUserList(){
         getonlineuserlist().then((response)=>{
             console.log("所有的学生列表")
             console.log(response)
             this.StudentData = response
            
             
         })
         getonlineteacherlist().then((response)=>{
               this.TeacherData = response
         })
    },
    voice() {
      this.noneVoice = !this.noneVoice
    },
    sendChatToAll() {
      this.socket.sendMessageToAll(
        {
          message: this.message,
          date: new Date(),
          username: this.socket.username
        },
        'chatToAll'
      )
      // TODO:保存到自己的历史
      // console.log('this.socket.baseData')
      // console.log(this.socket.baseData)
      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: this.message,
        date: new Date(),
        username: this.socket.username,
        isMe: true
      }

      // 保存到后台
      addChat(
        this.socket.username,
        this.socket.baseData.fromIp,
        this.message,
        this.socket.baseData.fromIdentity
      ).then((response) => {})

      console.log('chatToAll :')
      console.log(record)
      this.message = ''
      this.$store.dispatch('push_toAll', record)
    },
    clearHistory() {
      this.$store.dispatch('reset_toAll')
      this.initMain()
    },
    initMain() {
      getChatList().then((response) => {
        console.log(response)
        this.Historys = response
      })
    },
    bindSocketMessage() {
      this.socket.addMessageType('online', (body) => {
        // 处理上线回调
        this.$notify({
          title: '上线通知',
          message: 'ip:' + body.fromIp + '\n身份:' + body.fromIdentity,
          position: 'bottom-right'
        })
        this.loadData()
      })
      this.socket.addMessageType('offline', (body) => {
        // 处理上线回调
        this.$notify({
          title: '下线通知',
          message: 'ip:' + body.fromIp + '\n身份:' + body.fromIdentity,
          position: 'bottom-right'
        })
        this.loadData()
      })
    },
    loadData() {
      getPeoples()
        .then((res) => {
          const data = res
          if (data.code === 200) {
            console.log(1)
            this.teachers = data.data.teachers
            this.students = data.data.students
            console.log('data.data')
            console.log(this.teachers)
            this.$store.dispatch('set_people', data.data)
            this.initRTC()
          } else {
            console.log(2)
            Message.error(data.msg)
          }
        })
        .catch((err) => {
          console.log(err)
        })

      getResourceList().then((response) => {
        
        this.tableData = response
        console.log(response);
     
        


      })
    },
    isLocal(ip) {
      if (this.socket.localIp === ip) {
        return true
      } else {
        return false
      }
    },
    setIce(rtc, ip, identity) {
      const that = this
      return (e) => {
        if (e.candidate) {
          this.socket.sendMessageToOne(
            {
              type: 'candidate',
              sdpMLineIndex: e.candidate.sdpMLineIndex,
              candidate: e.candidate.candidate
            },
            'candidate',
            ip,
            identity
          )
          // console.log("sendMessagICE")
          // that.checkState()
        } else {
          // this.log = "candidate验证完成"
          console.log('ICE验证完成，rtcDescription:')
        }
      }
    },
    receiveResource() {
      // 接收分发的文件并自动下载
      if (this.roles[0] === 'student') {
        this.socket.addMessageType('distribute', (body) => {
          const data = body.data
          this.downloadFile(data.path, data.folder)
        })
        /*
		ipcRenderer.on('download-success', (event, arg) => {
          this.$notify({
            title: '下载成功',
            message: `保存路径:${this.folder}\n大小:${arg}bytes`,
            position: 'bottom-right'
          })
        })
		*/
      }
    },
    downloadFile(path, folder) {
      const that = this
      this.folder = folder
      const downloadAddress = `http://${this.serverIp}:${this.serverPort}${path}`
      ipcRenderer.send('download', downloadAddress + '+' + folder)
    },
  
    handleChange(val) {
      console.log(val)
    }
  },
  watch: {
    toAllHistory(newVal) {
      const e = document.getElementById('main')
      e.scrollTop = e.scrollHeight
    }
  },
  computed: {
    ...mapGetters(['name', 'roles', 'getRtc', 'stuRTC', 'vsocket']),
    ...mapGetters(['students', 'teachers', 'toAllHistory'])
  }
}
</script>
<style scoped>
.dashbord {
  display: grid;
  grid-template-columns: 30% 70%;
  height: 100vh;
  width: 1739px;
  /* background: #eeeeee; */
  background-image: linear-gradient(
    to top,
    lightgrey 0%,
    lightgrey 1%,
    #e0e0e0 26%,
    #efefef 48%,
    #d9d9d9 75%,
    #bcbcbc 100%
  );
  grid-gap: 20px;
  padding: 20px;
}

.dashbord-middle {
  display: grid;
  grid-template-columns: 100%;
  grid-template-rows: 70% 30%;
}
.dashbord-middle .card {
  height: 100%;
}

.dashbord-footer {
  display: grid;
  grid-template-rows: 100%;
  grid-template-columns: 25% 25% 25% 25%;
}
.charts {
  width: 100%;
}
#main,
#main2,
#main3,
#main4,
#earth {
  height: 100%;
  width: 100%;
}

.chat{
  overflow-y: scroll; height:80vh ;
  background: #8cb2d2;
  outline:10px solid rgb(124, 185, 235);
  box-shadow:0 0 0 5px rgb(87, 85, 102);
  height: 70vh;
  
}
</style>
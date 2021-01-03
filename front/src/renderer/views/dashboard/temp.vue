<template>
  <div class="dashboard-container" style="padding-top: 10px">
    <el-card class="box-card onlineDisplay" style="margin-bottom: 20px">
      <div slot="header" class="clearfix">
        <span>教师</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4" v-for="item in teachers" :key="item.id">
          <div class="grid-content bg-purple">
            <el-row type="flex" class="row-bg" justify="center">
              <img src="src/renderer/assets/在线.jpg" class="dashboard-img" />
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-tag>{{ item.username }}</el-tag>
              <el-tag type="danger" v-if="isLocal(item.ip)">本机</el-tag>
            </el-row>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card onlineDisplay">
      <div slot="header" class="clearfix">
        <span>学生</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4" v-for="item in students" :key="item.id">
          <div class="grid-content bg-purple">
            <el-row type="flex" class="row-bg" justify="center">
              <img src="src/renderer/assets/在线.jpg" class="dashboard-img" />
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-tag>{{ item.username }}</el-tag>
              <el-tag type="danger" v-if="isLocal(item.ip)">本机</el-tag>
            </el-row>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <el-button
      type="primary"
      @click="drawer = true"
      style="margin: 20px 0 5px 70px"
      >显示签到详情</el-button
    >
    <el-button
      type="primary"
      @click="opendrawer = true"
      style="margin: 20px 0 5px 70px"
      >通知消息</el-button
    >

    <!-- 下方图示 -->
    <div class="chartsDisArea">
      <!-- 上线人数统计 -->
      <div class="onlineCharts">
        <div class="onlineChartsItem">
          <div class="onlineChartsItemTitle">今日签到人数</div>
          <div class="onlineChartsItemNum">10</div>
        </div>
        <div class="onlineChartsItem">
          <div class="onlineChartsItemTitle">今日上线人数</div>
          <div class="onlineChartsItemNum">30</div>
        </div>
        <div class="onlineChartsItem">
          <div class="onlineChartsItemTitle">在线人数</div>
          <div class="onlineChartsItemNum">1</div>
        </div>
        <div class="onlineChartsItem">
          <div class="onlineChartsItemTitle">实验完成人数</div>
          <div class="onlineChartsItemNum">5</div>
        </div>
      </div>
      <!-- 历史签到 -->
      <div class="signInHistory">
        <div id="main" style="width: 100%; height: 100%"></div>
      </div>
    </div>

    <!-- 底下显示视频 -->
    <div class="box-card onlineDisplay" style="margin-top: 20px">
      <!-- <div id="localVideo" class="videoPattern"></div> -->
      <div
        v-for="video in videoList"
        :key="video.id"
        style="display: inline-block"
      >
        <div :id="video.userName" class="videoPattern"></div>
      </div>
    </div>
    <div id="console"></div>

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

    <el-drawer title="消息通知" :visible.sync="opendrawer" :with-header="false">
      <div
        style="width: 600px; height: 100%; background-color: #2b3343 !important"
      >
        <el-button type="primary" style="margin: 20px 0 5px 70px"
          >全体静音</el-button
        >
        <el-button type="primary" style="margin: 20px 0 5px 70px"
          >解除静音</el-button
        >
        <el-container class="chat">
          <el-container class="right">
            <el-main class="main" id="main">
              <div v-for="item in Historys" :key="item.id">
                <el-avatar
                  style="float: left; position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding: 10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{ item.userName }}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{ item.content }}</el-tag>
                </el-row>
              </div>
              <div v-for="item in toAllHistory" :key="item.id">
                <el-avatar
                  style="float: left; position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding: 10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{ item.username }}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{ item.message }}</el-tag>
                </el-row>
              </div>
              <div id="scroll" style="overflow: hidden"></div>
            </el-main>
            <el-footer class="footer">
              <el-input
                placeholder="请输入内容"
                v-model="message"
                @keydown.enter.native="sendChatToAll"
                style="width: 400px"
              >
                <span slot="prepend">发送消息：</span>
                <el-button
                  title="点击发送消息"
                  style="
                    background-color: #409eff;
                    font-size: 16px;
                    color: #ffffff;
                  "
                  slot="append"
                  @click="sendChatToAll"
                  icon="el-icon-s-promotion"
                ></el-button>
              </el-input>
            </el-footer>
          </el-container>
        </el-container>
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
const ipcRenderer = require('electron').ipcRenderer

const echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/bar')
import { getuserlist, getuserlistById } from '../../api/user'

import AgoraRtcEngine from 'agora-electron-sdk'
import path from 'path'
import os from 'os'
const shell = require('electron').shell
const APPID = process.env['AGORA_APPID'] || 'a0bec9a017144787a4976b9102ff9667'

export default {
  name: 'dashboard',
  data() {
    return {
      students: [],
      teachers: [],
      drawer: false,
      opendrawer: false,
      tableData: [],
      message: '',
      messageList: [],
      Historys: [],
      videoList: [],
      uid: ''
    }
  },

  watch: {
    toAllHistory(newVal) {
      const e = document.getElementById('main')
      e.scrollTop = e.scrollHeight
    }
  },
  created() {
    // 在这里为学生端绑定offer的函数
  },
  mounted() {
    this.initMain()
    this.loadData()
    this.bindSocketMessage()
    this.receiveResource()
    // 画图
    this.drawLine()
  },
  methods: {
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

      getuserlist().then((response) => {
        // console.log("----",response)
        this.videoList = response
        // console.log("+++",this.videoList)
        // console.log(this.socket.username)
      })

      getuserlistById(this.socket.username).then((response) => {
        // console.log("本机id",response)
        this.uid = response
        // 初始化videos
        this.$nextTick(function() {
          // Code that will run only after the
          // entire view has been rendered
          if (global.rtcEngine) {
            global.rtcEngine.release()
            global.rtcEngine = null
          }

          if (!APPID) {
            alert('Please provide APPID in App.vue')
            return
          }

          const consoleContainer = document.querySelector('#console')

          const rtcEngine = new AgoraRtcEngine()
          rtcEngine.initialize(APPID)

          // listen to events
          rtcEngine.on('joinedChannel', (channel, uid, elapsed) => {
            consoleContainer.innerHTML = `join channel success ${channel} ${uid} ${elapsed}`
            const localVideoContainer = document.querySelector(
              '#' + this.socket.username
            )
            // setup render area for local user
            rtcEngine.setupLocalVideo(localVideoContainer)
          })
          rtcEngine.on('error', (err, msg) => {
            consoleContainer.innerHTML = `error: code ${err} - ${msg}`
          })
          rtcEngine.on('userJoined', (uid) => {
            getuserlist().then((response) => {
              for (var i = 0; i < response.length; i++) {
                if (uid == response[i].id) {
                  // setup render area for joined user
                  const remoteVideoContainer = document.querySelector(
                    '#' + response[i].userName
                  )
                  rtcEngine.setupViewContentMode(uid, 1)
                  rtcEngine.subscribe(uid, remoteVideoContainer)
                  break
                }
              }
            })
          })

          // set channel profile, 0: video call, 1: live broadcasting
          rtcEngine.setChannelProfile(1)
          rtcEngine.setClientRole(1)

          // enable video, call disableVideo() is you don't need video at all
          rtcEngine.enableVideo()

          const logpath = path.join(os.homedir(), 'agorasdk.log')
          // set where log file should be put for problem diagnostic
          rtcEngine.setLogFile(logpath)

          // join channel to rock!
          rtcEngine.joinChannel(null, 'demoChannel', null, this.uid)

          global.rtcEngine = rtcEngine
        })
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
        // console.log(response);
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
    }
  },
  computed: {
    ...mapGetters(['name', 'roles', 'getRtc', 'stuRTC', 'vsocket']),
    ...mapGetters(['students', 'teachers', 'toAllHistory'])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    background-color: rgb(34, 41, 55) !important;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }

  &-img {
    width: 45px;
    height: 45px;
    margin-bottom: 5px;
  }
}
.onlineDisplay {
  width: 90%;
  margin: auto;
  background: #2b3343 !important;
}
.box-card {
  // background-color: rgb(34, 41, 55) !important;
  border: 0;
  color: white;
}
.chartsDisArea {
  height: 800px;
  width: 100%;
  // border: 1px solid black;
  padding-left: 5%;
}
.onlineCharts {
  width: 35%;
  height: 400px;
  // border: 1px solid black;
  // margin-left: 5%;
  margin-top: 10px;
  float: left;
}
.signInHistory {
  margin-top: 10px;
  width: 60%;
  height: 400px;
  float: left;
  // border: 1px solid black;
}
.onlineChartsItem {
  width: 190px;
  height: 190px;
  // border: 1px solid black;
  border-radius: 5px;
  margin: 5px;
  float: left;
  background-color: #df7401;
  color: #fff;
}
.onlineChartsItemTitle {
  margin-left: 25%;
  margin-top: 5px;
}
.onlineChartsItemNum {
  font-size: 30px;
  margin-top: 30%;
  margin-left: 40%;
}
.videoPattern {
  width: 120px;
  height: 120px;
  overflow: hidden;
  margin: 2px;
  border: 1px solid black;
}
</style>

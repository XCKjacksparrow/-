<template>
  <div class="dashboard-container">
    <el-card class="box-card">
      <el-row type="flex" class="row-bg" justify="center">
        <video
          id="local"
          style="width: 100%; height: 100%"
          :autoplay="true"
        ></video>
      </el-row>

      <el-dialog
  title="提示"
  :visible.sync="dialogVisible"
  width="30%"
  :before-close="handleClose">
  <span>讲解完毕，若需要继续演示，点击投屏按钮</span>
  <span slot="footer" class="dialog-footer">
   
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="stopDisplay">确 定</el-button>
  </span>
</el-dialog>

      <el-row style="text-align: center; margin-top: 10px">
        
        <el-button @click="dialogVisible = true" :disabled="!dispalying" type="danger"
          >停止</el-button
        >
        <el-button @click="startDisplay" :disabled="dispalying" type="success"
          >投屏</el-button
        >
      </el-row>

    </el-card>
    
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { setRtcIce, setAddTrack } from '../../utils/RTCUtils'
import { getPeoples } from '../../api/dashboard'
const { ipcRenderer, remote } = require('electron')
var desktopCapturer = require('electron').desktopCapturer
import MyWebRTC from '@/utils/webRTC'

import { getUserMedia } from '../../utils'

export default {
  name: 'dashboard',
  students: [],
  data() {
    return {
      dialogVisible: false,
      localStream: null,
      remoteStream: {},
      localVideo: null,
      remoteVideo: {},
      startFlag: false,
      dispalying: false,
      endFlag: true,
      rtc: null,
      rtcs: []
    }
  },
  mounted() {
    this.loadData()
    this.localVideo = document.getElementById('local')
    this.remoteVideo = document.getElementById('remote')
    if (this.teacher_remoteStream) {
      this.localVideo.srcObject = this.teacher_remoteStream
      this.startFlag = true
      this.dispalying = true
      this.endFlag = false
    }
  },
  methods: {
    handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
    link: (url) => {
      shell.openExternal(url)
    },
    loadData() {
      getPeoples()
        .then((res) => {
          const data = res
          if (data.code === 200) {
            console.log(1)
            this.teachers = data.data.teachers
            this.students = data.data.students
          } else {
            console.log(2)
            Message.error(data.msg)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },
    startDisplay() {
      // 首先获取到视频流，由于是异步的所以下一步在watch里
      remote.getCurrentWindow().minimize()
      this.getDesktop()

      getPeoples()
        .then((res) => {
          const data = res
          if (data.code === 200) {
            console.log(1)
            this.teachers = data.data.teachers
            this.students = data.data.students
          } else {
            console.log(2)
            Message.error(data.msg)
          }
        })
        .catch((err) => {
          console.log(err)
        })
      // console.log(this.students);
    },
    getDesktop() {
      const that = this
      getUserMedia((stream) => {
        that.localStream = stream
        // that.localVideo.src = URL.createObjectURL(stream)
        that.localVideo.srcObject = stream
        that.startFlag = true
        that.endFlag = false
        that.$store.dispatch('set_teacher_remoteStream', stream)
      })
    },
    connectRTCs() {
      this.$store.dispatch('rest_rtcpool')
      const that = this

      // 建立连接，老师对每个学生主动发起连接
      this.students.forEach((student) => {
        const studentIp = student.ip
        const identity = 'student'
        // 创建对这个学生的rtc实例
        const rtc = new MyWebRTC(that.socket, studentIp, identity)
        rtc.description =
          '这是与' + studentIp + identity + '的RTC连接实例的教师端'
        rtc.doConnection()
        this.socket.addMessageType('answer', (body) => {
          const fromIp = body.fromIp
          const fromIdentity = body.fromIdentity
          const data = body.data
          that.rtcs.some((rtc) => {
            if (rtc.remoteIp === fromIp) {
              rtc.rtcConnection
                .setRemoteDescription(new RTCSessionDescription(data))
                .then(() => {
                  console.log(
                    '----rtcremoteDesc设置完毕------这是特殊的answer处理方案'
                  )
                  console.log(rtc)
                })
            }
          })
        })
        this.socket.addMessageType('candidate', (body) => {
          const fromIp = body.fromIp
          const fromIdentity = body.fromIdentity
          const data = body.data
          that.rtcs.some((rtc) => {
            if (rtc.remoteIp === fromIp) {
              const candidate = new RTCIceCandidate({
                sdpMLineIndex: data.sdpMLineIndex,
                candidate: data.candidate
              })
              rtc.rtcConnection.addIceCandidate(candidate)
            }
          })
        })
        // 给rtc实例绑定一些回调
        rtc.rtcConnection.onicecandidate = that.setRtcIce(
          rtc,
          studentIp,
          identity
        )
        rtc.rtcConnection.ontrack = that.setAddTrack()
        // console.log(that.remoteVideo)
        // 开始添加视频流轨道
        this.localStream.getTracks().forEach((track) => {
          rtc.rtcConnection.addTrack(track, that.localStream)
        })
        // rtc.sendOffer()
        rtc.rtcConnection.createOffer().then((des) => {
          rtc.rtcConnection
            .setLocalDescription(des)
            .then(() => {
              that.socket.sendMessageToOne(des, 'offer', studentIp, identity)
              console.log('发送offer')
            })
            .catch((err) => {
              console.log(err)
            })
        })
        // that.$store.dispatch('push_rtcpool', rtc)
        that.rtcs.push(rtc)
      })
    },
    resetRTCs() {
      this.rtc.rtcConnection.close()
      this.RTCs.forEach((rtc) => {
        rtc.rtcConnection.close()
      })
      this.$store.dispatch('rest_rtcpool')
    },
    setRtcIce(rtc, ip, identity) {
      const that = this
      return (e) => {
        console.log('----onIceCandidate------')
        if (e.candidate) {
          that.socket.sendMessageToOne(
            {
              type: 'candidate',
              sdpMLineIndex: e.candidate.sdpMLineIndex,
              candidate: e.candidate.candidate
            },
            'candidate',
            ip,
            identity
          )
        } else {
          // this.log = "candidate验证完成"
          console.log('ICE验证完成，rtcDescription:' + rtc.description)
          console.log(rtc)
        }
      }
    },
    setAddTrack() {
      const that = this
      return (e) => {
        console.log('监测到对端添加了track')
        that.remoteStream = e.streams[0]
        that.remoteVideo.srcObject = that.remoteStream
      }
    },
    stopDisplay() {
      this.dialogVisible = false
      this.startFlag = false
      // 发送关闭消息 让对端清空rtc
      const that = this
      this.students.forEach((s) => {
        that.socket.sendMessageToOne(
          { data: 'null' },
          'stopRTC',
          s.ip,
          'student'
        )
      })
      if (this.localStream) {
        this.localStream.getTracks().forEach((track) => {
          track.stop()
        })
      }
      this.$store.dispatch('set_teacher_remoteStream', null)
      this.localVideo.srcObject = null
      // 清除rtc
      this.resetRTCs()
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'roles',
      'RTCs',
      'getRtc',
      'students',
      'teacher_remoteStream'
    ])
  },
  watch: {
    startFlag(newVal) {
      if (newVal) {
        // 开始投屏
        console.log(newVal)
        this.dispalying = true
        // 建立RTC连接
        this.connectRTCs()
      } else {
        // 结束 投屏
        this.dispalying = false
        this.endFlag = true
        // 清除rtc
        this.resetRTCs()
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    // margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
#local {
  object-fit: fill;
  border: 1px solid #ccc;
  background-color: #909399;
  min-height: 600px;
}

.video {
  width: 120px;
  height: 120px;
  overflow: hidden;
}
</style>

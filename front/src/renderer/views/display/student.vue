<template>
  <div class="dashboard-container">
    <el-card class="box-card">
      <el-row type="flex" class="row-bg" justify="center">
        <video id="remote" style="width: 100%;height:90%" :autoplay="true"></video>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import MyWebRTC from '@/utils/webRTC'

const desktopCapturer = require('electron').desktopCapturer
export default {
  name: 'dashboard',
  data() {
    return {
      localStream: null,
      localVideo: null,
      remoteVideo: null,
      remoteStream: null,
      rtc: null,
      startFlag: false,
      displaying: false,
      endFlag: true,
      connectionState: ['disconnected', 'failed', 'closed']
    }
  },
  created() {
    this.checkState()
  },
  mounted() {
    // 绑定停止RTC的函数
    const that = this
    this.socket.addMessageType('stopRTC', (body) => {
      that.stuRTC.rtcConnection.close()
      that.stuRTC.rtcConnection = null

      if (that.remoteStream !== undefined && that.remoteStream !== null) {
        that.remoteStream = null
      }

      that.rtc = null
      that.$store.dispatch('rest_stuRTC')
      that.$store.dispatch('rest_remoteStream')
      that.checkState()
      that.$notify({
        title: '系统通知',
        message: '老师关闭了投影。',
        position: 'bottom-right'
      })
      that.$router.push('../dashboard')
      this.reload()
    })

    // 获取DOM元素
    this.localVideo = document.getElementById('local')
    this.remoteVideo = document.getElementById('remote')
    console.log('学生端检测remote_stream')
    console.log(this.remote_stream)
    if (this.remote_stream) {
      this.remoteVideo.srcObject = this.remote_stream
      this.displaying = true
      console.log('true++++')
    }
    this.init()
  },
  methods: {
    checkState() {
      console.log(this.stuRTC)
      const rtc = this.$store.state.util.studentRtc
      rtc.rtcConnection.onicecandidate = this.setIce(
        rtc,
        rtc.remoteIp,
        rtc.remoteIdentity
      )
      rtc.rtcConnection.ontrack = this.setAddTrack(rtc)
      this.rtc = rtc
      this.$store.dispatch('set_stuRTC', rtc)
      // 首先进来的时候判断vuex中的RTC状态
      if (rtc) {
        // 绑定回调
        console.log('进入checkState')
        console.log(
          'rtc状态:connectionState=' + this.stuRTC.rtcConnection.connectionState
        )
        // 不是null、undefined、‘’，则判断RTC的连接状态
        const state = this.stuRTC.rtcConnection.connectionState
        let flag = false
        console.log('state:' + state)
        this.connectionState.forEach((s) => {
          if (state === s) {
            flag = true
          }
        })
        if (flag) {
          // 此标志表示连接状态异常，则startFlag = false
          this.startFlag = false
          this.endFlag = true
          this.displaying = false
        } else {
          this.startFlag = true
          if (state === 'connecting') {
            this.displaying = true
          } else {
            this.displaying = false
          }
          this.endFlag = false
        }
      } else {
        this.startFlag = false
        this.endFlag = true
        this.displaying = false
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
          that.checkState()
        } else {
          // this.log = "candidate验证完成"
          console.log('ICE验证完成，rtcDescription:')
        }
      }
    },
    setAddTrack(rtc) {
      const that = this
      return (e) => {
        console.log('监测到对端添加了track，开始取出')
        that.remoteStream = e.streams[0]
        // console.log(that.remoteStream)
        // console.log(e)
        that.remoteVideo.srcObject = this.remoteStream
        that.startFlag = true
        that.endFlag = false
        that.displaying = true
        that.$store.dispatch('set_remoteStream', e.streams[0])
      }
    },
    getUserMedia() {
      const that = this
      desktopCapturer.getSources({ types: ['screen'] }, function(
        error,
        sources
      ) {
        if (error) throw error
        sources.forEach((s) => {
          navigator.webkitGetUserMedia(
            {
              audio: false,
              video: {
                mandatory: {
                  chromeMediaSource: 'screen',
                  minWidth: 1280,
                  maxWidth: 1280,
                  minHeight: 720,
                  maxHeight: 720
                }
              }
            },
            (stream) => {
              that.localStream = stream
              that.localVideo.srcObject = stream
              stream.getTracks().forEach((track) => {
                that.rtc.rtcConnection.addTrack(track, stream)
              })
            },
            (err) => {
              console.log(err)
            }
          )
        })
      })
    }
  },
  computed: {
    ...mapGetters(['name', 'roles', 'teachers', 'stuRTC', 'remote_stream'])
  },
  inject: ['reload'],
  watch: {
    stuRTC(newVal) {
      this.checkState()
    },
    remote_stream(newVal) {
      console.log('检测到stream改变')
      console.log(newVal)
      if (newVal) {
        this.remoteVideo.srcObject = newVal
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
#remote {
  object-fit: fill;
  border: 1px solid #ccc;
  background-color: #909399;
  min-height: 600px;
}
</style>

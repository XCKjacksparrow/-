<template>
  <div class="dashboard-container">

    <el-container style="min-height:calc(100vh - 51.5px);box-shadow: 0 0 40px 2px #eee;">

      <!-- 左边视图 -->
      <el-main class="main1">
          <el-button @click="open()" type="primary">信息按钮</el-button>
             <el-row type="flex" class="row-bg" justify="center" >
                <video id="remote" style="width: 100%; height: 100%" :autoplay="true" src="C:\Users\amin\Desktop\虚拟仿真项目\3.打开广口瓶盖.mp4"></video>
            </el-row>
      </el-main>

    <!-- 右侧对话框 -->
     <el-aside style="width:30%;border:1px solid black; border-radius:5px;" class="aside"  v-show="isopen" >

          <el-container class="right">
            <el-main class="main1" id="main1" style="height:700px">
              <div v-for="item in Historys" :key="item.id">
                <el-avatar
                  style="float:left;position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份 -->
                <el-row style="padding:10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{item.userName}}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                 <el-row style="margin-left: 40px">
                  <el-tag type="info">{{item.content}}</el-tag>
                </el-row>
              </div>
              <div v-for="item in toAllHistory" :key="item.id">
                <el-avatar
                  style="float:left;position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding:10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{item.username}}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row> 
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{item.message}}</el-tag>
                </el-row>
              </div>
              <div id="scroll" style="overflow: hidden"></div>
            </el-main>

            <el-footer class="footer">
              <el-input
                placeholder="请输入内容"
                v-model="message1"
                @keydown.enter.native="sendChatToAll"
                style="width:300px"
              >
                <span slot="prepend">发送消息：</span>
                <el-button
                  title="点击发送消息"
                  style="background-color:#409EFF;font-size:10px;color:#FFFFFF"
                  slot="append"
                  @click="sendChatToAll"
                  icon="el-icon-s-promotion"
                ></el-button>
              </el-input>
            </el-footer>
          </el-container> 

      </el-aside> 
    </el-container>
    <!-- </el-card> -->
    <div id="showDiv" style="position: absolute; background-color: white; border: 1px solid black;"></div>
    <div id="content"></div>
  </div>
</template>

<script>
import { mapGetters, mapSetters } from 'vuex'

import { getChatList, addChat } from '../../api/chat'
import { getUserMedia } from '../../utils'
import ChatItem from '../../components/ChatItem'
import MyWebRTC from '../../utils/webRTC'
import Recorder from 'recorder-core'
import 'recorder-core/src/engine/mp3'
import 'recorder-core/src/engine/mp3-engine'
import Client from '../../utils/client'
export default {
  components: {
    ChatItem
  },
  data() {
    return {
      message1: '',
      messageList: [],
      Historys: [],
      // toIp: this.$route.query.ip,
      // toIdentity: this.$route.query.identity,
      isopen: false,
      rec: null,
      recBlob: null,
      client1: null,

      showDiv: null,
      remoteVideo: null,
      remoteStream: null,
      localStream: null,
      restaurants: [],
      state1: '',
      state2: '',
      active: 0,
      rtc: null,
      currentStep: null,
      turnState: null,
      staticMessage: [
        { '1': '坩埚放置铁架台，纸漏斗放置铁架台' },
        { '2': '取铝粉药剂瓶，取氧化铁药剂瓶，放置滤纸和钥匙' },
        { '3': '用钥匙取铝粉和氧化铁，氧化铁和铝粉比例3:2' },
        { '4': '将混合物倒入漏斗中' },
        { '5': '将镁条插入混合物中' },
        { '6': '点燃酒精灯' },
        { '7': '点燃镁条' }
      ],
      staticModel: [{ value: '请准备上课' }],
      stepInfo: {
        fromIp: '',
        fromIdentity: '',
        toIp: '',
        toIdentity: '',
        userName: '',
        type: 'stepinfo',
        data: {
          currentStep: '1',
          preScore: '10',
          grade_sum: '15'
        }
      },
      form: {
        title: '',
        content: '',
        type: '课堂通知'
      },
      message: {
        fromIp: '127.0.0.1',
        fromIdentity: 'teacher',
        message: 'test message',
        date: '2019-10-5 12:52:00',
        isMe: true
      },
      privateMessage: '',
      tableData: [
        { grade: 10, key: 1, description: '取出酒精灯' },
        { grade: 10, key: 2, description: '点火' },
        { grade: 0, key: 3, description: '实验完成' }
      ]
    }
  },
  props: ['dialogIp', 'dialogIdentity', 'studentName'],
  created() {
    // this.toIp = this.dialogIp
    // this.toIdentity = this.dialogIdentity
    // console.log('dialogIdentity' + this.studentName)
    // this.userName = this.studentName
    this.initMain()
  },

  methods: {
      initMain() {
      getChatList().then(response => {
        console.log(response)
        this.Historys = response
      })
    },
    sendChatToAll() {
      this.socket.sendMessageToAll(
        {
          message: this.message1,
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
        message: this.message1,
        date: new Date(),
        username: this.socket.username,
        isMe: true
      }

      // 保存到后台
      addChat(
        this.socket.username,
        this.socket.baseData.fromIp,
        this.message1,
        this.socket.baseData.fromIdentity
      ).then((response) => {})

      console.log('chatToAll :')
      console.log(record)
      this.message1 = ''
      this.$store.dispatch('push_toAll', record)
    },
    clearHistory() {
      this.$store.dispatch('reset_toAll')
    },
    evaluate() {
      this.$router.push({ path: '/Study/analysis' })
    },
    open() {
      console.log(111)
      this.isopen = !this.isopen
    },
    Call() {
      // 获取学生的ip
      const toip = this.toIp
      this.client1.callUser(toip)
    },
    HangUp() {
      this.client1.hangUp()
    },
    recOpen() {
      const that = this
      // 一般在显示出录音按钮或相关的录音界面时进行此方法调用，后面用户点击开始录音时就能畅通无阻了
      this.rec = null
      this.recBlob = null
      var newRec = Recorder({
        type: 'mp3',
        sampleRate: 16000,
        bitRate: 16, // mp3格式，指定采样率hz、比特率kbps，其他参数使用默认配置；注意：是数字的参数必须提供数字，不要用字符串；需要使用的type类型，需提前把格式支持文件加载进来，比如使用wav格式需要提前加载wav.js编码引擎
        onProcess: function(
          buffers,
          powerLevel,
          bufferDuration,
          bufferSampleRate,
          newBufferIdx,
          asyncEnd
        ) {}
      })
      newRec.open(
        function() {
          // 打开麦克风授权获得相关资源
          that.rec = newRec
          // 此处创建这些音频可视化图形绘制浏览器支持妥妥的
          console.log('已打开录音，可以点击录制开始录音了')

          if (that.rec && Recorder.IsOpen()) {
            that.recBlob = null
            that.rec.start()
            console.log('开始录音')
          } else {
            console.log('未打开录音')
          }
        },
        function(msg, isUserNotAllow) {
          // 用户拒绝未授权或不支持
          console.log(
            (isUserNotAllow ? 'UserNotAllow，' : '') + '打开录音失败：' + msg
          )
        }
      )
    },
    recClose() {
      const that = this
      if (this.rec) {
        // rec.start();
        // rec.close();
        this.rec.stop(
          function(blob, duration) {
            console.log(
              blob,
              (window.URL || webkitURL).createObjectURL(blob),
              '时长:' + duration + 'ms'
            )

            that.recBlob = blob
            console.log(
              '已录制mp3：' +
                duration +
                'ms ' +
                blob.size +
                '字节，可以点击播放、上传了'
            )

            // audio写入#content;
            that.recPlay()

            // socket.send(1 + (window.URL || webkitURL).createObjectURL(blob));
          },
          function(msg) {
            console.log('录音失败:' + msg)
          }
        )
      } else {
        console.log('未打开录音')
      }
    },
    /** 播放**/
    recPlay() {
      if (!this.recBlob) {
        console.log('请先录音，然后停止后再播放')
        return
      }
      // var audio = document.createElement("audio");
      // audio.controls = true;
      // document.querySelector("#content").appendChild(audio);
      // //简单利用URL生成播放地址，注意不用了时需要revokeObjectURL，否则霸占内存
      // audio.src = (window.URL || webkitURL).createObjectURL(this.recBlob);
      // // audio.play();

      // setTimeout(function() {
      //   (window.URL || webkitURL).revokeObjectURL(audio.src);
      // }, 5000);

      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: (window.URL || webkitURL).createObjectURL(this.recBlob),
        type: 'audio',
        date: new Date(),
        isMe: true
      }
      // 发送指导信息给实验环境：

      this.$message({
        type: 'success',
        message: '实验指导发送成功'
      })
      this.$store.dispatch('push_toOne', record)
    },
    init() {
      /**
       * 登录时页面步骤条
       */

      const fromIp = this.socket.baseData.fromIp
      const fromIdentity = this.socket.baseData.fromIdentity
      // 构建一个Client
      this.client1 = new Client(fromIp)
      setTimeout(() => {
        // console.log(this.client1.conn.stream)  本地流
        try {
          document.querySelector('#localAudio').srcObject = this.client1.conn.stream
        } catch (error) {
          document.querySelector('#localAudio').src = window.URL.createObjectURL(this.client1.conn.stream)
        }
      }, 2000)

      console.log('教师端ip' + fromIp)
      console.log('学生端ip' + this.toIp)
      const stepdata = this.stepInfos(this.toIp)
      console.log('stepdata')
      if (stepdata !== undefined && stepdata !== null) {
        console.log(stepdata)
        console.log(stepdata[stepdata.length - 1].currentStep)
        this.active = Number.parseInt(
          stepdata[stepdata.length - 1].currentStep
        )
      } else {
        console.log('该学生还未开始作业')
      }
      /** 步骤读取结束 */

      const that = this
      // console.log('toIp : '+ toIp)
      this.socket.addMessageType('monitorOffer', body => {
        const data = body.data
        console.log('monitorOffe进入')
        const rtc = new MyWebRTC(this.socket, this.toIp, this.toIdentity)
        console.log('monitorOffer正在执行')
        rtc.doConnection()
        this.rtc = rtc
        rtc.rtcConnection
          .setRemoteDescription(new RTCSessionDescription(data))
          .then(() => {
            console.log('----rtcremoteDesc设置完毕,开始回复answer------')
          })
        rtc.rtcConnection.onicecandidate = this.setIce(
          rtc,
          rtc.remoteIp,
          rtc.remoteIdentity
        )
        rtc.rtcConnection.ontrack = that.setAddTrack(rtc)
        getUserMedia(stream => {
          stream.getTracks().forEach(track => {
            rtc.rtcConnection.addTrack(track, stream)
          })
        })

        rtc.rtcConnection.createAnswer().then(des => {
          rtc.rtcConnection
            .setLocalDescription(des)
            .then(() => {
              that.socket.sendMessageToOne(
                des,
                'answer',
                rtc.remoteIp,
                rtc.remoteIdentity
              )
              console.log('发送answer完毕')
            })
            .catch(err => {
              console.log(err)
            })
        })
        that.socket.addMessageType('candidate', body => {
          const data = body.data
          const candidate = new RTCIceCandidate({
            sdpMLineIndex: data.sdpMLineIndex,
            candidate: data.candidate
          })
          rtc.rtcConnection.addIceCandidate(candidate)
        })

        that.$notify({
          title: '【实验指导】',
          message:
            // '身份:' + body.toIdentity + '\n正在对 ' + that.userName + ' 进行指导',
            '正在对 ' + that.userName + ' 进行实验指导',
          position: 'bottom-right'
        })

        this.remoteVideo = document.getElementById('remote')
        // console.log("检测remote_stream");
        // console.log(this.remote_stream);
        if (this.remote_stream) {
          this.remoteVideo.srcObject = this.remote_stream
          console.log('true++++')
        }
      })
    },
    stopDisplay() {
      console.log('停止教师端的连接')
      this.startFlag = false
      // 发送关闭消息 让对端清空rtc
      const that = this
      console.log('toIp:--------------')
      console.log(this.toIp)
      // that.socket.sendMessageToOne(({ data: 'null' }, 'stopRTC', this.toIp, 'student'))

      that.socket.sendMessageToOne(
        { data: 'null' },
        'stopMonitor',
        this.toIp,
        'student'
      )

      if (this.remote_stream) {
        this.remote_stream.getTracks().forEach(track => {
          track.stop()
        })
      }
      this.$store.dispatch('set_teacher_remoteStream', null)
      this.remote_stream.srcObject = null
      // 清除rtc
      this.rtc.rtcConnection.close()
    },

    getStream(stream) {
      const that = this
      // that.remoteVideo.srcObject = stream
      this.localStream = stream
      console.log(
        'this.toIp:' + this.toIp + ' this.toIdentity:' + this.toIdentity
      )
      const rtc = new MyWebRTC(this.socket, this.toIp, this.toIdentity)
      rtc.doConnection()
      rtc.rtcConnection.onicecandidate = this.setIce(
        rtc,
        rtc.remoteIp,
        rtc.remoteIdentity
      )
      rtc.rtcConnection.ontrack = this.setAddTrack(rtc)
      this.localStream.getTracks().forEach(track => {
        rtc.rtcConnection.addTrack(track, this.remoteStream)
      })
      this.rtc = rtc
      this.socket.sendMessage('', 'teacher')
      // this.rtc.sendMonitorOffer()
    },
    setAddTrack(rtc) {
      const that = this
      return e => {
        console.log('监测到对端添加了track，开始取出')
        that.remoteStream = e.streams[0]
        console.log(that.remoteStream)
        // console.log(e)
        that.remoteVideo.srcObject = this.remoteStream
        that.startFlag = true
        that.endFlag = false
        that.displaying = true
        that.$store.dispatch('set_remoteStream', e.streams[0])
      }
    },
    sendNotice(ip, identity) {
      // 向学生发送消息提醒
    },
    sendChatToOne() {
      // this.socket.sendMessage({ message: this.privateMessage, date: new Date() }, 'chatToOne')
      // TODO:保存到自己的历史
      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: this.privateMessage,
        type: 'msg',
        date: new Date(),
        isMe: true
      }
      // 发送指导信息给实验环境：

      const data = {
        monitor: this.privateMessage
      }
      this.socket.sendMessageToOne(data, 'monitorMessage', this.toIp, 'exp')
      this.$message({
        type: 'success',
        message: '实验指导发送成功'
      })
      this.$store.dispatch('push_toOne', record)
      // 已经显示在实验
      // if (this.privateMessage !== '') {
      //   this.form.title = '实验指导'
      //   this.form.content = this.privateMessage
      //   this.socket.sendMessage(this.form, 'release')
      // }
      // this.socket.sendMessageToOne({ message: this.privateMessage }, 'notice', this.toIp, this.toIdentity)
      this.$store.dispatch('push_release', this.form)
      this.message = this.toOneHistory
      this.privateMessage = ''
    },
    handleCommand(command) {
      const args = command.split('/')
      const ip = args[0]
      const identity = args[1]
      const message = this.menu[Number.parseInt(args[2])].message
      this.socket.sendMessageToOne({ message }, 'notice', ip, identity)
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.grade <= 0) {
        return 'danger-row'
      } else {
        return 'success-row'
      }
    },
    setIce(rtc, ip, identity) {
      const that = this
      return e => {
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
        } else {
          // this.log = "candidate验证完成"
          console.log('ICE验证完成，rtcDescription:')
        }
      }
    },
    expGuide(name) {
      console.log(name)
      alert('button click')
    },
    turnToStep() {
      // 跳转到指定步骤
      this.turnState = 'turnstart'
      let stepdata = this.stepInfos(this.toIp)
      console.log('跳转前总步骤数：' + this.stepInfos(this.toIp).length)

      stepdata === undefined ? (stepdata = [{ currentStep: 0 }]) : stepdata
      // 这里是不是应该让stepInfo再等回stepdata
      if (
        Number.parseInt(this.currentStep) <=
        Number.parseInt(stepdata[stepdata.length - 1].currentStep)
      ) {
        /**
         * 这里为了更新stepInfo，为什么插入的是从0开始到当前步骤的元素组;
         * reset了之前的记录不就没了么
         * 跳转之后步骤更新成绩也要更新，还想着从里面拿到当前步骤
         * 所以如果当前第六步跳转到第四步，你们是想只保留第一步到第三步的成绩，后面的就丢掉是么是的
         */
        // this.$store.dispatch("push_stepInfo", this.stepInfos(this.toIp).slice(0,Number.parseInt(this.currentStep)));

        this.$store.dispatch('set_stepInfo', {
          fromIp: this.toIp,
          data: this.stepInfos(this.toIp).slice(
            0,
            Number.parseInt(this.currentStep) - 1
          )
        })
        this.active = Number.parseInt(this.currentStep)
        console.log('跳转后步骤数：' + this.stepInfos(this.toIp).length)
      } else {
        this.$message({
          type: 'info',
          message: '该实验步骤未做，请重新选择'
        })
        return false
      }
      const data = {
        step: Number.parseInt(this.currentStep) + '',
        monitor: '已经跳转步骤至第' + this.currentStep + '步'
      }
      // this.socket.sendMessageToOne(data, 'jump', this.toIp, 'exp')
      this.$message({
        type: 'success',
        message: '跳转指令发送成功'
      })
      // 添加跳转步骤显示
      // 1.知识点加入到下拉菜单里面，课后增加一些东西，课前增加一些东西。
      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: '请注意实验操作，即将跳转步骤至第' + this.currentStep + '步',
        date: new Date(),
        isMe: true
      }
      this.$store.dispatch('push_toOne', record)
      this.socket.sendMessageToOne(data, 'monitorMessage', this.toIp, 'exp')

      // this.$message({
      //   type: 'success',
      //   message: '实验指导发送成功，等待学生查看'+data.monitor
      // })
    },

    querySearch(queryString, cb) {
      var restaurants = this.restaurants
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return restaurant => {
        return (
          restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        )
      }
    },
    handleSelect(item) {
      console.log(item)
    },

    loadAll() {
      return this.staticModel
    }

    // {"1":"坩埚放置铁架台，纸漏斗放置铁架台"},
    // {"2":"取铝粉药剂瓶，取氧化铁药剂瓶，放置滤纸和钥匙"},
    // {"3":"用钥匙取铝粉和氧化铁，氧化铁和铝粉比例3:2"},
    // {"4":"将混合物倒入漏斗中"},
    // {"5":"将镁条插入混合物中"},
    // {"6":"点燃酒精灯"},
    // {"7":"点燃镁条"}
  },
  computed: {
...mapGetters(['students', 'teachers', 'toAllHistory'])
  },

  watch: {
  }
}
</script>


<style scoped rel="stylesheet/scss" lang="scss">

.el-dropdown {
  vertical-align: top;
  margin-left: 2px;
}
.el-icon-arrow-down {
  font-size: 14px;
}

.dashboard {
  &-container {
    // margin: 30px;
    background-color: rgb(34, 41, 55) !important;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.main {
  padding: 20px;
  // border: 1px solid lightgrey;
  // box-shadow: 0 0 40px 1px #ccc;
  border-right: none;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;

  background-image: linear-gradient(
    to top,
    lightgrey 0%,
    lightgrey 1%,
    #e0e0e0 26%,
    #efefef 48%,
    #d9d9d9 75%,
    #bcbcbc 100%
  );
  height: 100vh;
  border: 0;
  width: 100%;
}

.aside {
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
}
</style>


<style>

.el-tabs__item.is-active {
  background-color: rgb(34, 41, 55) !important;
  color:#fff;
  border-radius: 5px;
  border:0;
}
.el-table .danger-row {
  background: #f56c6c;
}

.el-table .success-row {
  background: #f0f9eb;
}
.el-dialog__body {
  padding: 0 !important;
}
.el-input-group__prepend {
  padding-right: 2px !important;
  padding-left: 12px !important;
}
.el-dialog__close::before {
  font-size: 30px;
  box-shadow: 0 0 2px 2px #eee;
}
.el-dialog__headerbtn {
  top: 2px;
  right: 2px;
}
.el-select .el-input {
  width: 130px;
}
.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
.propper-font {
  font-size: 20px;
}
.record {
  padding: 7px;
  /* border: 1px solid black; */
}
.record:hover {
  cursor: pointer;
  /* border: 1px solid black; */
}
</style>

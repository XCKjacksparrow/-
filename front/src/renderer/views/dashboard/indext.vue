<template>
  <div class="dashbord">
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
    <div class="dashbord-left">
      <el-card>
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item title="课程信息 Course Information" name="1">
            <div>
              与现实生活一致：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；
            </div>
            <div>
              在界面中一致：所有的元素和结构需保持一致，比如：设计样式、图标和文本、元素的位置等。
            </div>
          </el-collapse-item>
          <el-collapse-item title="当前在线教师 Teacher" name="2">
            <el-table :data="TeacherData" stripe style="width: 100%">
              <el-table-column prop="name" label="姓名" width="180">
              </el-table-column>
              <el-table-column prop="phone" label="手机号" width="180">
              </el-table-column>
              <el-table-column prop="info" label="简介"> </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="当前在线学生 Student" name="3">
            <el-table :data="StudentData" stripe style="width: 100%">
              <el-table-column type="index" width="50"> </el-table-column>
              <el-table-column prop="name" label="姓名" width="180">
              </el-table-column>
              <el-table-column prop="num" label="学号" width="180">
              </el-table-column>
              <el-table-column prop="classes" label="班级"> </el-table-column>
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
    <div class="dashbord-middle">
      <div>
        <div id="earth"></div>
      </div>
      <div class="dashbord-footer">
        <div class="charts">
          <div id="main"></div>
        </div>
        <div class="charts">
          <div id="main2"></div>
        </div>
        <div class="charts">
          <div id="main3"></div>
        </div>
        <div class="charts">
          <div id="main4"></div>
        </div>
      </div>
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
const echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/pie')
require('echarts-gl')

export default {
  data() {
    return {
      students: [],
      teachers: [],
      tableData: [],
      message: '',
      messageList: [],
      Historys: [],
      videoList: [],
      uid: '',
      drawer: false,
      opendrawer: false,
      activeNames: ['1'],
      StudentData: [
        {
          num: '1181301201',
          name: '王小虎',
          classes: '计算机1185'
        },
        {
          num: '1181301201',
          name: '王小虎',
          classes: '计算机1185'
        },
        {
          num: '1181301201',
          name: '王小虎',
          classes: '计算机1185'
        },
        {
          num: '1181301201',
          name: '王小虎',
          classes: '计算机1185'
        }
      ]
    }
  },
  created() {},
  mounted() {
    // 画图
    this.drawEarth()
    this.drawLine1()
    this.drawLine2()
    this.drawLine3()
    this.drawLine4()
  },
  methods: {
    drawEarth() {
      const eart = this.$echarts.init(document.getElementById('earth'))
      this.eart = eart
      eart.setOption({
        backgroundColor: '#000',
        globe: {
          baseTexture: require('../../assets/image/earth.jpg'),
          heightTexture: require('../../assets/image/bathymetry_bw_composite_4k.jpg'),

          displacementScale: 0.1,

          shading: 'lambert',

          environment: require('../../assets/image/starfield.jpg'),

          light: {
            ambient: {
              intensity: 0.1
            },
            main: {
              intensity: 1.5
            }
          },

          layers: [
            {
              type: 'blend',
              blendTo: 'emission',
              texture: require('../../assets/image/night.jpg')
            },
            {
              type: 'overlay',
              texture: require('../../assets/image/clouds.png'),
              shading: 'lambert',
              distance: 5
            }
          ]
        },
        series: []
      }),
      window.addEventListener('resize', function() {
        eart.resize()
      })
    },
    drawLine1() {
      const myChart = this.$echarts.init(document.getElementById('main'))
      this.myChart = myChart

      myChart.setOption({
        backgroundColor: '#ffffff',

        // title: {
        //     text: 'Customized Pie',
        //     left: 'center',
        //     top: 20,
        //     textStyle: {
        //         color: '#ccc'
        //     }
        // },

        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [
              { value: 335, name: '直接访问' },
              { value: 400, name: '搜索引擎' }
            ].sort(function(a, b) {
              return a.value - b.value
            }),
            roseType: 'radius',
            label: {
              color: 'rgba(255, 255, 255, 0.3)'
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              },
              smooth: 0.2,
              length: 10,
              length2: 20
            },
            itemStyle: {
              color: '#c23531',
              shadowBlur: 200,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function(idx) {
              return Math.random() * 200
            }
          }
        ]
      }),
      window.addEventListener('resize', function() {
        myChart.resize()
      })
    },
    drawLine2() {
      const myChart2 = this.$echarts.init(document.getElementById('main2'))
      this.myChart2 = myChart2

      myChart2.setOption({
        backgroundColor: '#ffffff',

        // title: {
        //     text: 'Customized Pie',
        //     left: 'center',
        //     top: 20,
        //     textStyle: {
        //         color: '#ccc'
        //     }
        // },

        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [
              { value: 335, name: '直接访问' },
              { value: 400, name: '搜索引擎' }
            ].sort(function(a, b) {
              return a.value - b.value
            }),
            roseType: 'radius',
            label: {
              color: 'rgba(255, 255, 255, 0.3)'
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              },
              smooth: 0.2,
              length: 10,
              length2: 20
            },
            itemStyle: {
              color: '#c23531',
              shadowBlur: 200,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function(idx) {
              return Math.random() * 200
            }
          }
        ]
      }),
      window.addEventListener('resize', function() {
        myChart2.resize()
      })
    },
    drawLine3() {
      const myChart3 = this.$echarts.init(document.getElementById('main3'))
      this.myChart3 = myChart3

      myChart3.setOption({
        backgroundColor: '#ffffff',

        // title: {
        //     text: 'Customized Pie',
        //     left: 'center',
        //     top: 20,
        //     textStyle: {
        //         color: '#ccc'
        //     }
        // },

        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [
              { value: 335, name: '直接访问' },
              { value: 400, name: '搜索引擎' }
            ].sort(function(a, b) {
              return a.value - b.value
            }),
            roseType: 'radius',
            label: {
              color: 'rgba(255, 255, 255, 0.3)'
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              },
              smooth: 0.2,
              length: 10,
              length2: 20
            },
            itemStyle: {
              color: '#c23531',
              shadowBlur: 200,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function(idx) {
              return Math.random() * 200
            }
          }
        ]
      }),
      window.addEventListener('resize', function() {
        myChart3.resize()
      })
    },
    drawLine4() {
      const myChart4 = this.$echarts.init(document.getElementById('main4'))
      this.myChart4 = myChart4

      myChart4.setOption({
        backgroundColor: '#ffffff',

        // title: {
        //     text: 'Customized Pie',
        //     left: 'center',
        //     top: 20,
        //     textStyle: {
        //         color: '#ccc'
        //     }
        // },

        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [
              { value: 335, name: '直接访问' },
              { value: 400, name: '搜索引擎' }
            ].sort(function(a, b) {
              return a.value - b.value
            }),
            roseType: 'radius',
            label: {
              color: 'rgba(255, 255, 255, 0.3)'
            },
            labelLine: {
              lineStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
              },
              smooth: 0.2,
              length: 10,
              length2: 20
            },
            itemStyle: {
              color: '#c23531',
              shadowBlur: 200,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function(idx) {
              return Math.random() * 200
            }
          }
        ]
      }),
      window.addEventListener('resize', function() {
        myChart4.resize()
      })
    },
    handleChange(val) {
      console.log(val)
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
</style>
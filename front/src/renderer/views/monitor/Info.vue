<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <div class="clearfix title">
          <h2>实验指导</h2>
        </div>
        <el-row style="margin-bottom: 20px">
          <el-col :span="3">
            <!-- TODO 切换到进度条模式 -->
            <el-button
              v-if="this.showScreenOrBar"
              @click="switchToBar"
              type="primary"
              >切换到进度条模式</el-button
            >
            <el-button v-else @click="switchToBar" type="primary"
              >切换到全局模式</el-button
            >
          </el-col>
          <el-col :span="3">
            <el-button @click="openchat()" type="primary"
              >展开消息互动面板</el-button
            >
          </el-col>
        </el-row>
        <el-row :gutter="20" style="min-height: 500px">
          <!-- 进度条模式 -->
          <el-col
            :span="20"
            v-show="!showScreenOrBar"
            style="margin-bottom: 20px"
          >
            <el-table
              :data="tableData"
              :header-cell-style="tableHeaderColor"
              style="width: 100%; border-radius: 5px"
              :row-class-name="tableRowClassName"
              :default-sort="{ prop: 'total', order: 'descending' }"
            >
              <el-table-column
                prop="name"
                label="姓名"
                sortable
              ></el-table-column>
              <el-table-column
                :label="`步骤${convert(item.stepId)}/${item.score}`"
                v-for="item in expInfo.steps"
                :key="item.stepId"
                sortable
                :sort-method="sortEach"
              >
                <template slot-scope="slot">
                  <div class="outBorder">
                    <div
                      class="innerBorder"
                      :style="{
                        width: slot.row.scoreList[item.stepId - 1].perScore,
                        height: heightData,
                        background: slot.row.scoreList[item.stepId - 1].color,
                      }"
                    ></div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                label="总分"
                prop="total"
                sortable
              ></el-table-column>

              <el-table-column label="操作">
                <template slot-scope="slot">
                  <el-button
                    type="text"
                    @click="guide(slot.row.ip, 'student', slot.row.name)"
                    >实验指导</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </el-col>

          <el-col
            :span="6"
            v-for="(item, index) in students"
            :key="index"
            v-show="showScreenOrBar"
          >
            <el-row>
              <el-card shadow="hover">
                <el-row>
                  <span>学生：{{ item.username }}</span>
                </el-row>
                <el-image
                  style="width: 100%; height: 120px"
                  :src="arr[index]"
                  :preview-src-list="[arr[index]]"
                ></el-image>
                <el-row type="flex" justify="center">
                  <el-dropdown
                    size="mini"
                    split-button
                    type="primary"
                    style="margin-right: 8px; margin-top: 6px"
                    @click="sendNotice(item.ip, item.identity)"
                    @command="handleCommand"
                  >
                    发送提醒
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item
                        v-for="m in menu"
                        :command="`${item.ip}/${item.identity}/${m.key}`"
                        :key="m.id"
                        >{{ m.message }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </el-dropdown>
                  <el-button
                    size="mini"
                    style="margin-right: 8px; margin-top: 6px"
                    type="success"
                    @click="guide(item.ip, item.identity, item.username)"
                    >实验指导</el-button
                  >
                </el-row>
              </el-card>
            </el-row>
          </el-col>
          <!-- 互动栏  margin-left:300px;width:200px;-->
        </el-row>
      </el-card>
    </div>
    <div>
      <el-dialog
        :visible.sync="dialogFlag"
        :fullscreen="true"
        :before-close="stopDisplay"
        v-if="addDialogVisible"
        center
      >
        <div>
          <Guide
            :dialogIp="targetIp"
            :dialogIdentity="targetIdentity"
            :studentName="username"
          />
        </div>
      </el-dialog>
    </div>
    <el-drawer
      :visible.sync="chatDrawer"
      direction="rtl"
      size="50%"
    >
        <div class="clearfix titleChat">
          <h2>消息面板</h2>
        </div>
      <!-- <el-col :span="chatCol" :offset="chatOff"> -->
      <el-card>
          <el-main class="main chat" id="main" >
            <div v-for="item in toAllHistory" :key="item.id" >
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
            <!-- <div id="scroll" style="overflow: hidden"></div> -->
          </el-main>

      </el-card>
        <el-input
          placeholder="请输入内容"
          v-model="message"
          @keydown.enter.native="sendChatToAll"
          style="width: 800px;margin: 10px 10px 10px 10px "
        >
          <template slot="prepend">发送消息：</template>
          <el-button
            class="sendButton"
            title="点击发送消息"
            slot="append"
            @click="sendChatToAll"
            icon="el-icon-s-promotion"
          ></el-button>
        </el-input>
    </el-drawer>
  </div>
</template>


<script>
import { getuserlist } from '../../api/user'
import { mapGetters } from 'vuex'
import Guide from './Guide'
import Vue from 'vue'

import NumberToCHN from '../../utils/dictionary'
// import electron from 'electron'
// const path = require('path')
// let BrowserWindow = electron.remote.BrowserWindow
export default {
  components: {
    Guide
  },
  data() {
    return {
      chatDrawer: false,
      isopen: false,
      studentList: [],
      chatCol: '6',
      chatOff: '4',
      heightData: '5px',
      tableData: [],
      showScreenOrBar: true,
      toAllHistory: [],
      message: '',
      dialogFlag: false,
      targetIp: '',
      targetIdentity: '',
      addDialogVisible: false,
      username: '',
      menu: [
        {
          key: 0,
          message: '请立刻进入上课状态'
        },
        {
          key: 1,
          message: '请小心操作'
        },
        {
          key: 2,
          message: '请加快实验进度'
        }
        // {
        //   key: 3,
        //   message: '这一步做错了，请重新思考！'
        // }
      ],
      arr: []
    }
  },
  created() {
    this.getuserList()
  },
  mounted() {
    this.arr = this.snapshots
    this.init()
  },
  methods: {
    openchat() {
      // this.isopen = !this.isopen;
      this.chatDrawer = true
    },
    getuserList() {
      getuserlist().then((response) => {
        //  console.log(response)
        const tmp = []

        var i = 0
        var m = 0
        console.log(response[i].ip)
        console.log(response.length)
        for (var i; i < response.length; i++) {
          if (response[i].ip == null) {
            tmp[m] = response[i]
            console.log(tmp[m])
            m = m + 1
            console.log(m)
          }
        }
        console.log(this.studentList)
        this.studentList = tmp
      })
    },
    sortEach(obj1, obj2) {
      return obj1.scoreList[0].grade_sum - obj2.scoreList[0].grade_sum
    },

    // 修改tr样式
    tableRowClassName({ row, rowIndex }) {
      if (row.grade < 60) {
        return 'warning-row'
      } else if (row.grade === '未操作') {
        return 'grey-row'
      } else {
        return 'success-row'
      }
      row.index = rowIndex
    },
    // 修改header样式
    tableHeaderColor({ row, column, rowIndex, columnIndex }) {
      // if (columnIndex === 4) {
      //   return 'background-image:url(src/renderer/assets/boom2.jpg);background-repeat:no-repeat; background-size:100% 100%;-moz-background-size:100% 100%;}'
      // }
    },
    init() {
      this.tableData = [
        {
          ip: '192.168.0.106',
          name: 'xck',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 2
            },
            {
              currentStep: 2,
              grade_sum: 2
            },
            {
              currentStep: 3,
              grade_sum: 5
            },
            {
              currentStep: 4,
              grade_sum: 7
            },
            {
              currentStep: 5,
              grade_sum: 8
            },
            {
              currentStep: 6,
              grade_sum: 5
            },
            {
              currentStep: 7,
              grade_sum: 10
            }
          ],
          total: 80
        },

        {
          ip: '192.168.0.106',
          name: 'kz',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 10
            },
            {
              currentStep: 2,
              grade_sum: 10
            },
            {
              currentStep: 3,
              grade_sum: 8
            },
            {
              currentStep: 4,
              grade_sum: 18
            },
            {
              currentStep: 5,
              grade_sum: 9
            },
            {
              currentStep: 6,
              grade_sum: 20
            },
            {
              currentStep: 7,
              grade_sum: 20
            }
          ],
          total: 62
        },
        {
          ip: '192.168.0.106',
          name: 'wzj',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 8
            },
            {
              currentStep: 2,
              grade_sum: 7
            },
            {
              currentStep: 3,
              grade_sum: 6
            },
            {
              currentStep: 4,
              grade_sum: 11
            },
            {
              currentStep: 5,
              grade_sum: 8
            },
            {
              currentStep: 6,
              grade_sum: 16
            },
            {
              currentStep: 7,
              grade_sum: 16
            }
          ],
          total: 62
        },
        {
          ip: '192.168.0.106',
          name: 'sxy',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 7
            },
            {
              currentStep: 2,
              grade_sum: 7
            },
            {
              currentStep: 3,
              grade_sum: 6
            },
            {
              currentStep: 4,
              grade_sum: 12
            },
            {
              currentStep: 5,
              grade_sum: 6
            },
            {
              currentStep: 6,
              grade_sum: 10
            },
            {
              currentStep: 7,
              grade_sum: 10
            }
          ],
          total: 0
        },
        {
          ip: '192.168.0.106',
          name: 'pyx',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 10
            },
            {
              currentStep: 2,
              grade_sum: 10
            },
            {
              currentStep: 3,
              grade_sum: 10
            },
            {
              currentStep: 4,
              grade_sum: 20
            },
            {
              currentStep: 5,
              grade_sum: 10
            },
            {
              currentStep: 6,
              grade_sum: 20
            },
            {
              currentStep: 7,
              grade_sum: 20
            }
          ],
          total: 62
        },
        {
          ip: '192.168.0.106',
          name: 'fqp',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 9
            },
            {
              currentStep: 2,
              grade_sum: 9
            },
            {
              currentStep: 3,
              grade_sum: 9
            },
            {
              currentStep: 4,
              grade_sum: 18
            },
            {
              currentStep: 5,
              grade_sum: 8
            },
            {
              currentStep: 6,
              grade_sum: 18
            },
            {
              currentStep: 7,
              grade_sum: 19
            }
          ],
          total: 62
        },
        {
          ip: '192.168.0.106',
          name: 'ly',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              grade_sum: 7
            },
            {
              currentStep: 2,
              grade_sum: 7
            },
            {
              currentStep: 3,
              grade_sum: 8
            },
            {
              currentStep: 4,
              grade_sum: 13
            },
            {
              currentStep: 5,
              grade_sum: 6
            },
            {
              currentStep: 6,
              grade_sum: 18
            },
            {
              currentStep: 7,
              grade_sum: 16
            }
          ],
          total: 62
        }
      ]

      this.tableData.forEach((element) => {
        var all = 0
        const fullScore = [10, 10, 10, 20, 10, 20, 20]
        element.scoreList.forEach((e) => {
          all = all + e.grade_sum
          var per =
            parseFloat(e.grade_sum) / parseFloat(fullScore[e.currentStep - 1])
          e.perScore = (per * 100).toString() + '%'
          if (per <= 0.2) {
            e.color = '#FF0000'
          } else if (per <= 0.4) {
            e.color = '#FF4000'
          } else if (per <= 0.6) {
            e.color = '#FF8000'
          } else if (per <= 0.8) {
            e.color = '#BFFF00'
          } else {
            e.color = '#80FF00'
          }
        })
        element.total = all
      })
    },
    switchToBar() {
      // 切换到进度条模式
      if (this.showScreenOrBar == true) {
        this.showScreenOrBar = false
        this.chatCol = 4
        this.chatOff = 0
      } else {
        this.showScreenOrBar = true
        this.chatCol = 6
        this.chatOff = 10
      }
    },
    sendChatToAll() {
      // this.socket.sendMessageToAll({ message: this.message, date: new Date(), username: this.socket.username }, 'chatToAll')
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
      this.toAllHistory.push(record)
      console.log('chatToAll :')
      console.log(record)
      this.message = ''
      // this.$store.dispatch('push_toAll', record)
    },
    sendNotice(ip, identity) {
      this.$prompt('请输入提醒内容', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
        .then(({ value }) => {
          this.$message({
            type: 'success',
            message: `发送成功！\n消息内容:${value}\n发送对象:${ip}\n对方身份:${identity}`
          })
          const data = {
            monitor: { value }
          }
          this.socket.sendMessageToOne(
            { message: value },
            'notice',
            ip,
            identity
          )
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          })
        })
    },
    shape() {
      this.$router.push('Info')
    },
    charSet() {
      this.$router.push('infoHistory')
    },
    handleCommand(command) {
      const args = command.split('/')
      const ip = args[0]
      const identity = args[1]
      const message = this.menu[Number.parseInt(args[2])].message
      // alert(ip+identity+message)
      console.log(message)
      this.socket.sendMessageToOne({ message }, 'notice', ip, identity)
      const data = {
        monitor: this.menu[Number.parseInt(args[2])].message
      }
      this.socket.sendMessageToOne(data, 'monitorMessage', ip, 'exp')
    },
    resetArr(newVal) {
      const newArr = []
      let flag = 0
      for (let i = 0; i < newVal.length; i++) {
        newVal.some((obj) => {
          // 寻找第0个数组元素
          // console.log(obj)
          if (this.getIndex(obj.ip) === flag) {
            // 找到了则加入newArr
            Vue.set(this.arr, flag, obj.image)
            flag++
            return true
          }
        })
      }
    },
    // 找到这个ip在students中的index
    getIndex(ip) {
      let result = 0
      this.students.some((s, index) => {
        if (s.ip === ip) {
          result = index
          return true
        }
      })
      return result
    },
    guide(ip, identity, username) {
      console.log('进入实验指导' + ip + identity + username)
      // this.$router.push(`/monitor/guide?ip=${ip}&identity=${identity}`)
      this.targetIp = ip
      this.targetIdentity = identity
      this.username = username
      console.log(this.targetIp)
      // let guidWindow = new BrowserWindow({show: false})
      // guidWindow.maximize()
      // guidWindow.show()
      // path.join(Guide)
      // this.socket.sendMessageToOne({ message: value }, 'notice', ip, identity)
      this.dialogFlag = true
      this.addDialogVisible = true
    },
    stopDisplay() {
      // 停止学生端的单向流
      this.addDialogVisible = false
      this.dialogFlag = false
      this.$store.dispatch('reset_toOne', null)
    }
  },
  computed: {
    ...mapGetters([
      'students',
      'getSnapShots',
      'snapshots',
      'stuRTC',
      'toOneHistory',
      'expInfo',
      'stepInfos',
      'stepInfosList'
    ]),

    convert() {
      const that = this
      return (num) => {
        const NumberToCHN = {
          1: '一',
          2: '二',
          3: '三',
          4: '四',
          5: '五',
          6: '六',
          7: '七',
          8: '八',
          9: '九',
          10: '十'
        }
        return NumberToCHN[num]
      }
    },
    getArr() {
      return (ip) => {
        return this.arr.filter((obj) => {
          console.log(obj.ip === ip)
          console.log(obj)
          return obj.ip === ip
        })
      }
    }
  },
  inject: ['reload'],
  watch: {
    snapshots(newVal) {
      // console.log('重组arr')
      this.resetArr(newVal)
    }
    // toAllHistory(newVal) {
    //   const e = document.getElementById("main");

    //   e.scrollTop = e.scrollHeight;
    // },
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.clearfix > h2 {
  text-align: center;
}
.mod-align {
  margin-left: 27%;
}
.title {
  background-image: linear-gradient(to right, #74ebd5 0%, #9face6 100%);
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}
.box-card {
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
.titleChat{
  background: #85a8ca;
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}
</style>
<style scoped>
.el-dialog__header {
  padding: 0;
}
.chat{
  overflow-y: scroll; height:80vh ;
  background: #8cb2d2;
  outline:10px solid rgb(124, 185, 235);
  box-shadow:0 0 0 5px rgb(87, 85, 102);
  height: 70vh;
  
}
div /deep/ .el-input-group__prepend{
  color: #000;
  background: #4592fb;
}
.sendButton{
}
</style>
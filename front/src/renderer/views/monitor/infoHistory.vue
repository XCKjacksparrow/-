<template>
    <div>
        <div class="dashboard-container">
            <el-card class="box-card" shadow="never">
               <div slot="header" class="clearfix" >
                 <!--     <el-row style="margin-bottom:10px">
                  <el-button type="primary" @click="shape()">图形导调</el-button>
                  <el-button type="primary" @click="charSet()">文字导调</el-button>
                </el-row> -->
                </div>

                <el-row type="flex" justify="center">
                    <el-table
                            :data="tableData"
                            style="width: 100%"
                            :row-class-name="tableRowClassName">
                        <!-- <el-table-column
                                prop="date"
                                label="日期"
                                >
                        </el-table-column> -->
                        <el-table-column
                                prop="name"
                                label="姓名"
                                >
                        </el-table-column>
                        <el-table-column
                              ref="cell"
                                :label="`步骤${convert(item.stepId)}/${item.score}`" v-for="item in expInfo.steps" :key="item.stepId">
                                <template slot-scope="slot">
                                  <el-alert
                                  
                                  :type="`${getScore(slot.row.name,item.stepId)}`"
                                  show-icon 
                                  :closable="false"
                                  class="step"
                                  >
                                 <!-- &&this.stepInfos(slot.row.ip).currentStep===item.stepId -->
                                 <el-dropdown v-if="getStep(slot.row.ip)===item.stepId" @command="handleCommand">
                                <span class="el-dropdown-link">
                                  <i class="el-icon-arrow-down el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown" >
                                  <el-dropdown-item  v-for="m in item.dangerMessage" :command="`${slot.row.ip}/student/${m}`">{{m}}</el-dropdown-item>
                                </el-dropdown-menu>
                              </el-dropdown>
                                </el-alert>
                                <!-- <el-tooltip class="item" effect="dark" :content=item.dangerMessage placement="top" v-else-if="item.isDanger" @click="">
                                   <el-alert
                                  type=getScore(slot.row.name,item.stepId)
                                  show-icon 
                                  :closable="false"
                                  class="step"
                                  style="font-size:40px"
                                  >
                                 
                                </el-alert>
                                </el-tooltip> -->
                                
                                 <!-- <el-dropdown size="mini" split-button type="primary" style="margin:0;padding:0;"
                                                 @click="sendNotice(slot.row.ip,'student')" @command="handleCommand" v-if="item.isDanger">
                          
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item v-for="m in item.dangerMessage" :command="`slot.row.ip/'student'/${m.key}`">{{m}}
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown> -->
                                
                            </template>
                        </el-table-column>
                        <el-table-column label="总分">
                          <template slot-scope="slot">
                              <el-tag type="success">{{slot.row.scoreList[slot.row.scoreList.length-1].grade_sum}}</el-tag>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="slot">
                                <el-button type="text" @click="guide(slot.row.ip,'student',slot.row.name)"> 实验指导</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-row>

            </el-card>
        </div>
        <el-dialog
            :visible.sync="dialogFlag"
            :fullscreen="true"
            v-if="addDialogVisible"
            center
            :before-close="stopDisplay"
            @close="closeCallback">
            <div>
              <Guide :dialogIp="targetIp" :dialogIdentity="targetIdentity" :studentName="username"/>

            </div>
            <!-- <span slot="footer" class="dialog-footer">
              <el-button @click="centerDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
            </span> -->
          </el-dialog>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Guide from '../monitor/Guide'
import NumberToCHN from '../../utils/dictionary'
export default {
  components: {
    Guide
  },
  data() {
    return {
      NumberToCHN,
      alertType: 'success',
      addDialogVisible: false,
	  username: '',
      dangerType: {
        'normal': 'inherit',
        'middle': '#ff7979',
        'heigh': '#e80808'
      },
      tableData: [
        // { date: '2019-10-06 12:52:00', name: '陈森', grade: '85' },
        // { date: '2019-10-06 12:52:00', name: '丁工程', grade: '65' },
      ],
      dialogFlag: false,
      targetIp: '',
      targetIdentity: ''
    }
  },
  mounted() {
    this.initData()
    console.log(this.NumberToCHN)
    this.parseJSON()
    console.log('stepInfos：')
    // console.log(this.stepInfos("192.168.56.1").length)
    // console.log(this.getStep())
  },
  inject: ['reload'],
  methods: {
    sendNotice(ip, identity) {
      this.$prompt('请输入提醒内容', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$message({
          type: 'success'
          // message: `发送成功！\n消息内容:${value}\n发送对象:${ip}\n对方身份:${identity}`
        })
        const data = {
          monitor: ({ value })
        }
        this.socket.sendMessageToOne({ message: value }, 'notice', ip, identity)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    closeCallback() {
      this.initData()
    },
    handleCommand(command) {
      const args = command.split('/')
      const ip = args[0]
      const identity = args[1]
      const message = args[2]
      // alert(ip+identity+message)
      // console.log('执行了handlecommand ' + ip + '  ' + identity + ' ' + message)
      this.socket.sendMessageToOne({ message }, 'notice', ip, identity)
      // console.log('执行了handlecommand')
      const data = {
        monitor: message
      }
      this.socket.sendMessageToOne(data, 'monitorMessage', ip, 'exp')
    },
    parseJSON() {
      const nodes = { ...this.stepError }

      // let cellElement = this.$refs.cell
      const cellElement = document.getElementsByClassName('cell')
      // console.log('cellElement')
      // console.log(cellElement)
      Object.keys(nodes).map(key => {
        setTimeout(() => {
          cellElement[key].style.color = this.dangerType[nodes[key].danger]
          let tempIndex = cellElement.length / 10
          tempIndex--
          while (tempIndex > 0) {
            const sum = 10 * tempIndex + Number.parseInt(key)
            // cellElement[sum].append("<button>23</button>")
            cellElement[sum].style.color = this.dangerType[nodes[key].danger]
            tempIndex--
          }
        }, 100)

        // cellElement[key].$el.style.color = "red"
        // cellElement
      })
    },
    shape() {
      this.$router.push('Info')
    },
    charSet() {
      this.$router.push('infoHistory')
    },
    initData(list) {
      const arr = []
      if (list == null || list == undefined) {
        list = this.stepInfosList
      }
      // console.log(list)
      this.students.forEach(s => {
        let initState = false
        Object.keys(list).forEach(ip => {
          console.log('ip=' + ip)
          if (s.ip === ip) {
            initState = true

            const scoreList = list[ip]
            console.log(scoreList)
            arr.push({
              ip: s.ip,
              name: s.username,
              date: '2019-11-15',
              scoreList
            })
            // break
          }
        })
        if (!initState) {
          arr.push({
            ip: s.ip,
            name: s.username,
            date: '2019-11-15',
            scoreList: [{ currentStep: 0, preScore: '----', grade_sum: 0 }]
          })
        }
      })

      this.tableData = arr
      // console.log("表格数据:")
      // console.log(this.tableData)
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.grade < 60) {
        return 'warning-row'
      } else if (row.grade === '未操作') {
        return 'grey-row'
      } else {
        return 'success-row'
      }
    },
    sumGrade(arr) {
      let sum = 0
      arr.forEach(g => {
        sum += g.grade
      })
      return arr[arr.length - 1].preScore
    },
    guide(ip, identity, name) {
      this.targetIp = ip
      this.targetIdentity = identity
	  this.username = name
      this.dialogFlag = true
      this.addDialogVisible = true
    },
    stopDisplay() { // 停止学生端的单向流
      this.addDialogVisible = false
      this.dialogFlag = false
      this.reload()
    }
  },
  computed: {
    ...mapGetters(['stepInfosList', 'stepInfos', 'students', 'expInfo', 'stepError']),
    //  stepWatch() {
    //   const that = this
    //   this.students.forEach(s => {
    //     const arr = that.stepInfos(s.ip)
    //     if(!arr)return true
    //     arr.forEach(g => {
    //       this.tableData.push({
    //       date: '2019-10-06 12:52:00',
    //       name: s.ip,
    //       grade: arr ? g.preScore : '未操作'
    //     })

    //   })

    //   })
    //   // return this.stepInfos(this.toIp)
    // },
    getStep() {
      return (ip) => {
        let step = 0
        let arr = this.stepInfos(ip)
        arr = arr || []
        arr.forEach(g => {
          step = g.currentStep
        })
        let tempNum = Number.parseInt(step) + 1
        if (tempNum > 7) {
          tempNum = 0
        }
        console.log('step：' + step)
        return tempNum + ''
      }
    },
    convert() {
      const that = this
      return (num) => {
        return this.NumberToCHN[num]
      }
    },
    getScore() {
      return (name, stepId) => {
        let result = 'info'
        this.tableData.some(item => {
          // console.log(item.name+"    "+name +"  "+item.scoreList.length)
          console.log('item.scoreList:')

          if (item.name === name) {
            // return Number.parseInt(stepId)>item.scoreList.length?"----":item.scoreList[stepId-1].preScore

            if (!(Number.parseInt(stepId) > Number.parseInt(item.scoreList[item.scoreList.length - 1].currentStep)) && Number.parseInt(item.scoreList[item.scoreList.length - 1].currentStep) > 0) {
              // console.log(Number.parseInt(item.scoreList[Number.parseInt(stepId)-1].preScore))
              if (Number.parseInt(item.scoreList[Number.parseInt(stepId) - 1].preScore) > 9) {
                result = 'success'
              } else {
                result = 'error'
              }
            }
          }
        })
        return result
      }
    },
    stepInfosListArg() {
      return this.stepInfosList
    }
  },
  watch: {
    stepWatch() {
      // console.log('实验结果界面检测步骤更新')
    },
    stepInfosListArg(newVal) {
      this.initData(newVal)
      // console.log('aaaaaaaaaaaaaaaaaaaaaaaaaa')
    }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
    .dashboard {
        &-container {
            // margin: 30px;
        }

        &-text {
            font-size: 30px;
            line-height: 46px;
        }
    }
    .clearfix>h2{
      text-align: center;
    }
    .step {
      background: none
    }
    
</style>

<style>
    .el-table .warning-row {
        background: oldlace;
    }
.el-alert__icon.is-big{font-size: 16.8px}
    .el-table .success-row {
        background: #f0f9eb;
    }

    .el-table .grey-row:hover{
      background: #ececec;
    }
</style>

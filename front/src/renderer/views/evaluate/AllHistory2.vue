<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <!-- title -->
        <div  class="clearfix title">
          <h2>学生实验步骤成绩</h2>
        </div>

        <!-- content -->
        <!-- <el-row type="flex" justify="center" style="height:800px"> -->
        <div class="content">
          <el-table
            :data="tableData"
            :cell-style="changeCellStyle"
            style="width: 100%; border-radius:5px"
            :row-class-name="tableRowClassName"
            :column-class-name="tableColumnClassName"
          >
            <!-- <el-table-column
                                prop="date"
                                label="日期"
                                >
            </el-table-column>-->
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column
              :label="`步骤${convert(item.stepId)}/${item.score}`"
              v-for="item in expInfo.steps"
              :key="item.stepId"
            >
              <template slot-scope="slot">
                <!-- {{getScore(slot.row.name,item.stepId)}} -->

                <div class="outBorder">
                  <div
                    class="innerBorder"
                    :style="{width: slot.row.scoreList[item.stepId-1].preScore,height: heightData,background:slot.row.scoreList[item.stepId-1].color}"
                  >
                    <!-- {{slot.row.scoreList[item.stepId-1].preScore}}  -->
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="总分" prop="total">
              <!-- <template slot-scope="slot">
                <el-tag
                  type="success"
                >{{slot.row.scoreList[(slot.row.scoreList.length)-1].grade_sum}}</el-tag>
                {{slot.row.total}}
              </template>-->
            </el-table-column>
            <!-- <el-table-column label="操作">
              <template slot-scope="slot">
                <el-button type="text" @click="guide(slot.row.ip,'student',slot.row.name)">实验指导</el-button>
              </template>
            </el-table-column> -->
          </el-table>
        </div>
        <!-- </el-row> -->
      </el-card>
    </div>
    <el-dialog
      :visible.sync="dialogFlag"
      :fullscreen="true"
      :before-close="stopDisplay"
      v-if="addDialogVisible"
      center
      @close="closeCallback"
    >
      <div>
        <Guide :dialogIp="targetIp" :dialogIdentity="targetIdentity" :studentName="username" />
      </div>
      <!-- <span slot="footer" class="dialog-footer">
              <el-button @click="centerDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
      </span>-->
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
      addDialogVisible: false,
      username: '',
      tableData: [
        // { date: '2019-10-06 12:52:00', name: '陈森', grade: '85' },
        // { date: '2019-10-06 12:52:00', name: '丁工程', grade: '65' },
      ],
      dialogFlag: false,
      targetIp: '',
      targetIdentity: '',
      widthData: '10%',
      heightData: '5px'
    }
  },
  mounted() {
    this.initData()
    // console.log(this.NumberToCHN);  //
  },
  methods: {
    changeCellStyle(row, column, rowIndex, columnIndex) {
      // 修改列样式
      if (row.column.label === '步骤四/20') {
        // return 'background-image:url(src/renderer/assets/boom2.jpg);background-repeat:no-repeat; background-size:100% 100%;-moz-background-size:100% 100%;}'
        // return "background-color:red"
      }
    },
    closeCallback() {
      this.initData()
    },
    initData(list) {
      const arr = []
      if (list == null || list == undefined) {
        list = this.stepInfosList
      }
      // console.log(list)
      this.students.forEach((s) => {
        let initState = false
        Object.keys(list).forEach((ip) => {
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

      // this.tableData = arr;

      this.tableData = [
        {
          ip: '192.168.0.106',
          name: 'xck',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
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
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
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
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
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
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            }
          ],
          total: 62
        },
        {
          ip: '192.168.0.106',
          name: 'pyx',
          date: '2019-11-15',
          scoreList: [
            {
              currentStep: 1,
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
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
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
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
              preScore: '40%',
              grade_sum: 10,
              color: '#FF4000'
            },
            {
              currentStep: 2,
              preScore: '50%',
              grade_sum: 20,
              color: '#FF4000'
            },
            {
              currentStep: 3,
              preScore: '80%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 4,
              preScore: '20%',
              grade_sum: 20,
              color: '#FF0000'
            },
            {
              currentStep: 5,
              preScore: '70%',
              grade_sum: 20,
              color: '#BFFF00'
            },
            {
              currentStep: 6,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            },
            {
              currentStep: 7,
              preScore: '100%',
              grade_sum: 20,
              color: '#80FF00'
            }
          ],
          total: 62
        }
      ]
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
    tableColumnClassName({ column, columnIndex }) {
      if ((columnIndex = 44)) {
        return 'boom'
      }
    },
    sumGrade(arr) {
      let sum = 0
      arr.forEach((g) => {
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
    stopDisplay() {
      // 停止学生端的单向流
      this.addDialogVisible = false
      this.dialogFlag = false
      this.reload()
    },
    renderProcessBar() {
      // 渲染进度条
      var processBar = document.getElementsByClassName('outBorder')[0]
      // for(var i = 0; i < processBar.length; i++){
      //   processBar[i].innerText = '10';
      // }
      console.log('processBar[0] : ' + processBar)
    }
  },
  inject: ['reload'],
  computed: {
    ...mapGetters(['stepInfosList', 'stepInfos', 'students', 'expInfo']),
    convert() {
      const that = this
      return (num) => {
        return this.NumberToCHN[num]
      }
    },
    getScore() {
      return (name, stepId) => {
        console.log('name:' + name)
        let result = '---'
        this.tableData.some((item) => {
          if (item.name === name) {
            // return Number.parseInt(stepId)>item.scoreList.length?"----":item.scoreList[stepId-1].preScore
            if (!(Number.parseInt(stepId) > item.scoreList.length)) {
              result = item.scoreList[stepId - 1].preScore
              // console.log("true")
            }
            return true
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
      console.log('实验结果界面检测步骤更新')
    },
    stepInfosListArg(newVal) {
      this.initData(newVal)
    }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.dashboard {

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.clearfix > h2 {
  text-align: center;
}

.outBorder {
  border: 1px solid black;
  border-radius: 5px;
}
.innerBorder {
  background-color: greenyellow;
  height: 50%;
  border-radius: 5px;
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
.title {
  background-image: linear-gradient(to right, #74ebd5 0%, #9face6 100%);
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}

.content {
  height: 600px;
  border-radius: 5px;
}
</style>

<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.el-table .grey-row:hover {
  background: #ececec;
}
.el-table .boom {
  background: red;
}
</style>

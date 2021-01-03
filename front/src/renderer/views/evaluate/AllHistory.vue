<template>
  <div>
      <el-card class="box-card" shadow="never">
        <div class="clearfix title">
          <h2>学生实验步骤成绩</h2>
        </div>
        <div class="content">
          <el-table
            :data="tableData"
            :cell-style="changeCellStyle"
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
            <el-table-column label="总分" prop="total" sortable>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <div v-if="scope.$index == 0">
                  <el-button id="analysisBtn" type="text" @click="drawer = true"
                    >分析</el-button
                  >
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
      <el-drawer
        title="成绩分析"
        :visible.sync="drawer"
        :direction="direction"
        @opened="openDraw"
        :before-close="handleClose"
      >
        <div id="chart" style="height: 100%; width: 600px"></div>
      </el-drawer>
    <el-dialog
      :visible.sync="dialogFlag"
      :fullscreen="true"
      :before-close="stopDisplay"
      v-if="addDialogVisible"
      center
      @close="closeCallback"
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
</template>

<script>
import { mapGetters } from 'vuex'
import Guide from '../monitor/Guide'
import NumberToCHN from '../../utils/dictionary'
const echarts = require('echarts/lib/echarts')
require('echarts/lib/chart/line')
require('echarts/lib/component/legend')
export default {
  components: {
    Guide
  },
  data() {
    return {
      average: [],
      high: [],
      low: [],
      widthStyle: '600px',
      NumberToCHN,
      addDialogVisible: false,
      username: '',
      tableData: [],
      dialogFlag: false,
      targetIp: '',
      targetIdentity: '',
      widthData: '10%',
      heightData: '5px',
      drawer: false,
      direction: 'rtl'
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    openDraw() {
      this.drawLine()
    },
    drawLine() {
      const chart = echarts.init(document.getElementById('chart'))
      chart.setOption({
        title: {
          text: '折线图堆叠'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['平均分', '最低分', '最高分']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: [
            '步骤一',
            '步骤二',
            '步骤三',
            '步骤四',
            '步骤五',
            '步骤六',
            '步骤七'
          ]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '平均分',
            type: 'line',
            data: this.average
          },
          {
            name: '最低分',
            type: 'line',
            data: this.low
          },
          {
            name: '最高分',
            type: 'line',
            data: this.high
          }
        ]
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then((_) => {
          done()
        })
        .catch((_) => {})
    },
    sortEach(obj1, obj2) {
      return obj1.scoreList[0].grade_sum - obj2.scoreList[0].grade_sum
    },
    // 修改header样式
    tableHeaderColor({ row, column, rowIndex, columnIndex }) {
      // if (columnIndex === 4) {
      //   return "background-image:url(src/renderer/assets/boom2.jpg);background-repeat:no-repeat; background-size:100% 100%;-moz-background-size:100% 100%;}";
      // }
    },

    changeCellStyle(row, column, rowIndex, columnIndex) {
      // 修改列样式
      if (row.column.label === '步骤四/20') {
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
          name: '夏崇康',
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
          name: '杨超',
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
          name: '王江南',
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
          name: '潘宇轩',
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
          name: '王紫剑',
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
          name: '孙湘源',
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
          name: '李平',
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
      for (let i = 0; i < 7; i++) {
        let sum = 0
        let lowNum = this.tableData[i].scoreList[0].grade_sum
        let highNum = this.tableData[i].scoreList[0].grade_sum
        this.tableData.forEach((element) => {
          sum = sum + element.scoreList[i].grade_sum
          if (element.scoreList[i].grade_sum >= highNum) {
            highNum = element.scoreList[i].grade_sum
          }
          if (element.scoreList[i].grade_sum <= lowNum) {
            lowNum = element.scoreList[i].grade_sum
          }
        })
        const ave = parseFloat(sum) / parseFloat(this.tableData.length)
        if (i == 3 || i == 5 || i == 6) {
          this.low.push(lowNum)
          this.high.push(highNum)
          this.average.push(ave)
        } else {
          this.low.push(lowNum * 2)
          this.high.push(highNum * 2)
          this.average.push(ave * 2)
        }
      }
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
            if (!(Number.parseInt(stepId) > item.scoreList.length)) {
              result = item.scoreList[stepId - 1].preScore
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
  
  border: 0;
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
.clearfix > h2 {
  text-align: center;
}
.content {
  height: 600px;
  border-radius: 5px;
}
</style>

<style>
.el-drawer.rtl {
  /* overflow: scroll; */
  width: 45% !important;
}
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

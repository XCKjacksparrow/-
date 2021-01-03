<template>
  <div>
    <el-card class="box-card" shadow="never">
      <!-- title -->
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
          <el-table-column prop="name" label="姓名" sortable></el-table-column>
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
      <div>
        <header>
          <h2>
            <span type="primary">推荐课程</span>
          </h2>
        </header>
        <div>
          <el-row>
            <el-col :span="4" v-for="edu in edulist" :key="edu.id" :offset="1">
              <el-card
                :body-style="{ padding: '0px', height: '360px' }"
                shadow="hover"
                style="width: 260px; height: 260px"
              >
                <div style="padding: 6px; height: 310px">
                  <div>
                    <div
                      style="position: relative; top: 15px; text-align: center"
                    >
                      {{ edu.title }}
                    </div>
                  </div>
                  <div style="position: relative; top: 30px">
                    <el-image :src="edu.cover" fit="fit" style="height:138px;width:250px"></el-image>
                    <div style="position: relative; top: 10px; left: 66px">
                      <!-- <i class="el-icon-time"></i>{{ project.pdatesave }} -->
                    </div>
                  </div>
                  <div style="position: relative; top: 45px">
                    &nbsp;&nbsp;<i class="el-icon-view"></i><span>9999</span>
                    <el-button type="text" style="float: right"
                      ><font size="4" @click="startstudy">开始学习</font></el-button
                    >
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <br/>
          <br/>
          <el-row >
            <el-col :span="4" :offset="12">
              <transition name="transitionName">
                <el-link type="success" @click="goAllClass" :underline="false" ><span class="el-icon-caret-right" style="font-size:20px" >全部课程</span></el-link>
              </transition>
              
            </el-col>
          </el-row>
        </div>
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
import { getlist } from '../../api/index'
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
      edulist: [],
      average: [],
      high: [],
      low: [],
      widthStyle: '600px',
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
      heightData: '5px',
      drawer: false,
      direction: 'rtl'
    }
  },
  created() {
    this.getclassteacher()
  },
  mounted() {
    this.initData()
  },
  methods: {
    goAllClass() {
      this.$router.push({ path: '/study/data' })
    },
    startstudy(id) {
      this.$router.push({ path: '/study/test/' + id })
    },
    getclassteacher() {
      getlist().then((response) => {
        this.edulist = response.data.eduList
        console.log(this.edulist[0])
      })
    },
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
      if (columnIndex === 4) {
        // return 'background-image:url(src/renderer/assets/boom2.jpg);background-repeat:no-repeat; background-size:100% 100%;-moz-background-size:100% 100%;}'
      }
    },

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
        }
      ]

      // 计算成绩百分比并显示
      // 100-81 #80FF00
      // 80-61 #BFFF00
      // 60-41 #FF8000
      // 40-21 #FF4000
      // 20-0 #FF0000
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
      // for(var i = 0; i < processBar.length; i++){
      //   processBar[i].innerText = '10';
      // }
      console.log('processBar[0] : ' + processBar)
    }
  },
  inject: ['reload'],
  computed: {
    ...mapGetters(['stepInfosList', 'stepInfos', 'students', 'expInfo']),
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
    stepWatch() {},
    stepInfosListArg(newVal) {
      this.initData(newVal)
    }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
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

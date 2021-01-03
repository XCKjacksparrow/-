<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <h2>我的成绩</h2>
        </div>

        <el-row type="flex" justify="center">
          <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
            <!-- <el-table-column prop="date" label="日期"></el-table-column> -->
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column
                                :label="`步骤${convert(item.stepId)}/${item.score}`" v-for="item in expInfo.steps" :key="item.stepId">
                                <template slot-scope="slot">
                                {{getScore(slot.row.name,item.stepId)}}
                                
                            </template>
                        </el-table-column>
                        <el-table-column label="总分">
                          <template slot-scope="slot">
                              <el-tag type="success">{{slot.row.scoreList[slot.row.scoreList.length-1].grade_sum}}</el-tag>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="slot">
                                <el-button type="text" @click="guide(slot.row.name,'student')"> 实验指导</el-button>
                            </template>
                        </el-table-column>
            <el-table-column prop="grade" label="评价"></el-table-column>
            <!-- <el-table-column label="操作">
                            <tempate slot-scope="slot">
                                <el-button type="text"> 修改</el-button>
                            </tempate>
            </el-table-column>-->
          </el-table>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import NumberToCHN from '../../utils/dictionary'
export default {
  data() {
    return {
      NumberToCHN,
      tableData: []
    }
  },
  mounted() {
    const that = this
    const arr = that.stepInfos(this.socket.fromIp)
    if (!arr) return true
    this.tableData.push({
      date: '2019-10-06 12:52:00',
      name: s.ip,
      grade: arr ? that.sumGrade(arr) : '未操作'
    })
  },
  methods: {
    closeCallback() {
      this.initData()
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
              name: s.username,
              date: '2019-11-15',
              scoreList
            })
            // break
          }
        })
        if (!initState) {
          arr.push({
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
      return arr[arr.length - 1].grade_sum
    }
  },
  computed: {
    ...mapGetters(['stepInfosList', 'stepInfos', 'students', 'expInfo']),
    getScore() {
      return (name, stepId) => {
        console.log('name:' + name)
        let result = '----'
        this.tableData.some(item => {
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
    convert() {
      const that = this
      return (num) => {
        return that.NumberToCHN[num]
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
      console.log('aaaaaaaaaaaaaaaaaaaaaaaaaa')
    }
  }
}
</script>

<style scoped>
</style>

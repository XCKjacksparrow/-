<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
          <h2>当堂评价</h2>
        </div>

        <!-- <el-row type="flex" justify="center">
                    No data
        </el-row>-->
        <el-row type="flex" justify="center">
          <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
            <el-table-column prop="date" label="日期"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="grade" label="评价"></el-table-column>
            <el-table-column label="操作">
              <tempate slot-scope="slot">
                <el-button type="text">修改</el-button>
              </tempate>
            </el-table-column>
          </el-table>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
//   tableData:[],
  data() {
    return {
      tableData: []
    }
  },
  mounted() {
    const that = this
    this.students.forEach(s => {
      const arr = that.stepInfos(s.ip)
      if (!arr) return true

      this.tableData.push({
        date: '2019-10-06 12:52:00',
        name: s.username,
        grade: arr ? that.sumGrade(arr) : '未操作'
      })
    })
  },
  methods: {

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
        console.log('row.grade:' + g.grade)
        sum += Number.parseInt(g.grade)
      })
      return arr[arr.length - 1].grade_sum
    }
  },
  computed: {
    ...mapGetters(['stepInfosList', 'stepInfos', 'students', 'expInfo']),

    stepWatch() {
      // current是选择跳转的步骤
      console.log('执行了stepWatch:active ' + this.tableData)// 不能删
      // if(this.currentStep!==null  ){
      //     this.active = Number.parseInt(this.currentStep)
      //   return null
      // }
      return this.stepInfos(this.toIp)
    }
  },
  watch: {
    stepWatch(newVal) {
      const that = this
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
.clearfix > h2 {
  text-align: center;
}
</style>

<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>

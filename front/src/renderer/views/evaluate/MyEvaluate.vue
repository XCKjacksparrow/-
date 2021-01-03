<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <div  class="clearfix title">
          <h2>当堂评价</h2>
        </div>

        <el-row type="flex" justify="center">
          <el-table :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
            <el-table-column prop="date" label="日期"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
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
export default {
  data() {
    return {
      tableData: []
    }
  },
  mounted() {
    const that = this

    const arr = that.stepInfos(this.socket.toIp)
    console.log(that.stepInfos(this.socket.toIP))
    if (!arr) return true
    this.tableData.push({
      date: '2019-10-06 12:52:00',
      name: s.ip,
      grade: arr ? that.sumGrade(arr) : '未操作'
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
        sum += g.grade
      })
      return arr[arr.length - 1].grade_sum
    }
  },
  computed: {
    ...mapGetters(['stepInfosList', 'stepInfos', 'students']),
    // sumGrade(arr) {
    //   let sum = 0;
    //   arr.forEach(g => {
    //     sum += g.grade;
    //   });
    //   return sum;
    // },
    baseDataWatch() {
      // current是选择跳转的步骤
      console.log('执行了stepWatch:baseData ' + this.baseData)// 不能删
      return this.stepInfos(this.toIp)
    }
  },
  watch: {

    baseDataWatch(newVal) {
      console.log(newVal)
      const l = newVal.length - 1
      this.active = Number.parseInt(newVal[l].currentStep)
      console.log('stepInfos更新' + newVal[l].currentStep)
    }
  }
}
</script>

<style scoped>
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
</style>

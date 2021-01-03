<template>
    <div>
        <div class="dashboard-container">
            <el-card class="box-card" shadow="never">
                <div slot="header" class="clearfix" >
                    <h2>实验成绩统计</h2>
                </div>
                <el-card style="margin-bottom: 10px;" shadow="never" >
<div id="myChart" :style="{width: '300px', height: '300px'}"></div>
                </el-card>
                <el-row type="flex" class="row-bg" justify="center" style="min-height:500px">

                </el-row>
            </el-card>

        </div>
    </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import { getResourceList, deleteResource, downloadFile } from '../../api/table'
  const ipcRenderer = require('electron').ipcRenderer
  // 引入基本模板
const echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/bar')
// 引入提示框和title组件
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')
export default {
  name: 'hello',
  data() {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
  },
  mounted() {
      this.drawLine()
  },
  methods: {
      drawLine() {
      // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('myChart'))
        // 绘制图表
        myChart.setOption({
          title: { text: '课堂实验步骤成绩' },
          tooltip: {},
          xAxis: {
            data: ['步骤一', '步骤二', '步骤三', '步骤四', '步骤五', '步骤六']
          },
          yAxis: {},
          series: [{
            name: '成绩',
            type: 'bar',
            data: [5, 20, 10, 15, 10, 20]
          }]
        })
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
</style>

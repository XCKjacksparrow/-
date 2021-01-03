<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <!-- title -->
        <div class="clearfix title">
          <h2>实验分析</h2>
        </div>
        <!-- charts display area -->
        <el-card class="chartsDisArea" shadow="never">
          <h2 class="titleOne">请选择实验日期:</h2>
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <div class="classStatics">
            <h2 class="titleOne">历史成绩对比</h2>
            <div
              class="chartDisplay"
              id="classStaticsByknowledgePoints"
              :style="chartDisplayCss"
            ></div>
            <div
              class="chartDisplay"
              id="studentSignIn"
              :style="chartDisplayCss"
            ></div>
            <h2 class="titleOne">提分攻略</h2>
            <template>
              <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="date" label="日期" width="180">
                </el-table-column>
                <el-table-column prop="name" label="學習預警" width="180">
                </el-table-column>
                <el-table-column prop="address" label="提分小技巧">
                </el-table-column>
              </el-table>
            </template>
          </div>
        </el-card>
        <el-row
          type="flex"
          class="row-bg"
          justify="center"
          style="min-height: 500px"
        ></el-row>
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
require('echarts/lib/chart/pie')
require('echarts/lib/chart/line')
require('echarts/lib/component/polar')
require('echarts/lib/component/legend')
export default {
  name: 'hello',
  data() {
    return {
      msg: 'Welcome to Your Vue.js App',
      chartDisplayCss: {
        width: '600px',
        height: '300px',
        display: 'inline-block',
        margin: '30px'
      },
      tableData: [
        {
          date: '2020-05-02',
          name: '蒸发与结晶操作不當',
          address: '多查閲數據第九章，以及認真的完成本章課後習題'
        },
        {
          date: '2020-05-04',
          name: '氧气的制作操作不當',
          address: '多查閲數據第八章，以及認真的完成本章課後習題'
        },
        {
          date: '2020-05-01',
          name: '固体药品的取用操作不當',
          address: '多查閲數據第七章，以及認真的完成本章課後習題'
        },
        {
          date: '2020-05-03',
          name: '物质的加热操作不當',
          address: '多查閲數據第六章，以及認真的完成本章課後習題'
        }
      ],
      options: [
        {
          value: '选项1',
          label: '2019-2020第一學期'
        },
        {
          value: '选项2',
          label: '2019-2020第二學期'
        },
        {
          value: '选项3',
          label: '2010-2021第一學期'
        },
        {
          value: '选项4',
          label: '2010-2021第二學期'
        }
      ],
      value: '选项1'
    }
  },
  mounted() {
    this.drawLine()
  },
  methods: {
    drawLine() {
      const classStaticsByknowledgePoints = echarts.init(
        document.getElementById('classStaticsByknowledgePoints')
      )
      const studentSignIn = echarts.init(
        document.getElementById('studentSignIn')
      )

      classStaticsByknowledgePoints.setOption({
        title: { text: '成績對比' },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['最低分', '最高分', '平均分']
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
            '蒸发与结晶',
            '氧气的制作',
            '固体药品的取用',
            '物质的加热',
            '溶液的配制'
          ]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '最低分',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230]
          },
          {
            name: '平均分',
            type: 'line',
            stack: '总量',
            data: [220, 182, 191, 234, 290, 330]
          },
          {
            name: '最高分',
            type: 'line',
            stack: '总量',
            data: [150, 232, 201, 154, 190, 330]
          }
        ]
      })

      studentSignIn.setOption({
        backgroundColor: '#29323c',

        title: {
          text: '知識點統計',
          left: 'center',
          top: 20,
          textStyle: {
            color: '#ccc'
          }
        },

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
              { value: 335, name: '酒精灯的使用' },
              { value: 310, name: '氧气的制作' },
              { value: 274, name: '固体药品的取用' },
              { value: 235, name: '物质的加热' },
              { value: 400, name: '溶液的配制' }
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
      })
      const seriesLabel = {
        normal: {
          show: true,
          textBorderColor: '#333',
          textBorderWidth: 2
        }
      }
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
.chartsDisArea {
  margin-bottom: 10px;
  background: linear-gradient(
      to bottom,
      rgba(255, 255, 255, 0.15) 0%,
      rgba(0, 0, 0, 0.15) 100%
    ),
    radial-gradient(
        at top center,
        rgba(255, 255, 255, 0.4) 0%,
        rgba(0, 0, 0, 0.4) 120%
      )
      #989898;
  background-blend-mode: multiply, multiply;

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

  border: 0;
  width: 100%;
}
.clearfix > h2 {
  text-align: center;
}
.chartDisplay {
  margin-left: 30px;
  // background-color: #2b3343 !important;
  background-color: white !important;
  border: 1px solid black;
  border-radius: 5px;
}

// 图表标题
.titleOne {
  color: white;
}

// .fade-enter {
//   opacity: 0;
// }
// .fade-enter-active {
//   transition: opacity 3s;
// }
// .fade-leave-to {
//   opacity: 0;
// }
// .fade-leave-active {
//   transition: opacity 3s;
// }
</style>

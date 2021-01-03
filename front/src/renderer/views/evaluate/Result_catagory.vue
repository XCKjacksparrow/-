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
          <h2 class="titleOne">请选择实验场次:</h2>
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <div class="classStatics">
            <h2 class="titleOne">分类统计</h2>
            <h3 class="titleOne">按知识点得分汇总</h3>
            <div class="chartDisplay" id="parallelClassByGrades3" :style="chartDisplayCss"></div>
            <!-- <div class="chartDisplay" id="classStaticsByknowledgePoints" :style="chartDisplayCss"></div> -->
            <h3 class="titleOne">出错情况分类汇总</h3>
            <!-- <div class="chartDisplay" id="classStaticsByErrorTypes" :style="chartDisplayCss"></div> -->
            <!-- <h3 class="titleOne">按实验时间汇总</h3> -->

            <!-- <div class="chartDisplay" id="knowledgePoints" :style="chartDisplayCss"></div>
            <div class="chartDisplay" id="studentSignIn" :style="chartDisplayCss"></div>-->
            <!-- <div
              class="chartDisplay"
              id="stepScores"
              :style="{width: '600px', height: '300px',display:'inline-block',margin:'30px'}"
            ></div>-->
            <div class="chartDisplay" id="classStaticsByErrorTypes" :style="chartDisplayCss"></div>
          </div>

          <!-- <h2 class="titleOne">同类实验分析</h2>
          <h3 class="titleOne">平行班成绩对比</h3>
          <div class="chartDisplay" id="parallelClassByGrades" :style="chartDisplayCss"></div>
          <h3 class="titleOne">平行班知识点对比</h3>
          <div class="chartDisplay" id="parallelClassByKnowledges" :style="chartDisplayCss"></div>

          <h2 class="titleOne">历史数据分析</h2>
          <h3 class="titleOne">学生历史成绩分析</h3>-->
          <!-- <h3 class="titleOne">教学评价</h3> -->
          <!-- <div class="chartDisplay" id="parallelClassByGrades2" :style="chartDisplayCss"></div> -->
        </el-card>
        <el-row type="flex" class="row-bg" justify="center" style="min-height:500px"></el-row>
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
      options: [
        {
          value: '选项1',
          label: '蒸发与结晶'
        },
        {
          value: '选项2',
          label: '氧气的制作'
        },
        {
          value: '选项3',
          label: '固体药品的取用'
        },
        {
          value: '选项4',
          label: '物质的加热'
        },
        {
          value: '选项5',
          label: '溶液的配制'
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
      // 基于准备好的dom，初始化echarts实例
      // 按知识点得分汇总

      // const parallelClassByGrades2 = echarts.init(
      //   document.getElementById("parallelClassByGrades2")
      // );
      const parallelClassByGrades3 = echarts.init(
        document.getElementById('parallelClassByGrades3')
      )
      const classStaticsByErrorTypes = echarts.init(
        document.getElementById('classStaticsByErrorTypes')
      )

      // const knowledgePoints = echarts.init(
      //   document.getElementById("knowledgePoints")
      // );
      // const studentSignIn = echarts.init(
      //   document.getElementById("studentSignIn")
      // );
      // const stepScores = echarts.init(document.getElementById("stepScores"));

      classStaticsByErrorTypes.setOption({
        title: { text: '实验人数' },
        xAxis: {
          type: 'category',
          data: ['器具的摆放', '操作顺序', '实验原理掌握', '器具本身损坏'],
          axisLabel: {
            interval: 0,
            rotate: 40
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [120, 190, 150, 80],
            type: 'bar',
            name: '出错人数',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(220, 220, 220, 0.8)'
            }
          }
        ]
      })
      // 绘制图表

      // parallelClassByGrades2.setOption({
      //   title: {
      //     text: "蒸发与结晶",
      //   },
      //   tooltip: {
      //     trigger: "axis",
      //   },
      //   legend: {
      //     data: ["平均分", "最高分", "最低分"],
      //   },
      //   grid: {
      //     left: "3%",
      //     right: "4%",
      //     bottom: "3%",
      //     containLabel: true,
      //   },
      //   toolbox: {
      //     feature: {
      //       saveAsImage: {},
      //     },
      //   },
      //   xAxis: {
      //     type: "category",
      //     boundaryGap: true,
      //     data: [ "酒精灯的使用",
      //       "氧气的制作",
      //       "固体药品的取用",
      //       "物质的加热",
      //       "溶液的配制",
      //       "蒸发与结晶",
      //       "盐水的配置",
      //       ],
      //     axisLabel: {
      //       interval: 0,
      //       rotate: 40,
      //     },
      //   },
      //   yAxis: {
      //     type: "value",
      //   },
      //   series: [
      //     {
      //       name: "平均分",
      //       type: "line",
      //       data: [75, 76, 77, 75, 75, 76, 87],
      //     },
      //     {
      //       name: "最高分",
      //       type: "line",
      //       data: [98, 99, 99, 99, 97, 89, 99],
      //     },
      //     {
      //       name: "最低分",
      //       type: "line",
      //       data: [56, 66, 55, 55, 65, 56, 45],
      //     },
      //   ],
      // });

      parallelClassByGrades3.setOption({
        title: {
          text: '蒸发与结晶'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['平均分', '最高分', '最低分']
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
          boundaryGap: true,
          data: [
            '酒精灯的使用',
            '氧气的制作',
            '固体药品的取用',
            '物质的加热',
            '溶液的配制',
            '蒸发与结晶',
            '盐水的配置'
          ],
          axisLabel: {
            interval: 0,
            rotate: 40
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '平均分',
            type: 'line',
            data: [75, 76, 77, 75, 75, 76, 87]
          },
          {
            name: '最高分',
            type: 'line',
            data: [98, 99, 99, 99, 97, 89, 99]
          },
          {
            name: '最低分',
            type: 'line',
            data: [56, 66, 55, 55, 65, 56, 45]
          }
        ]
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
.chartsDisArea {
  margin-bottom: 10px;
  background: linear-gradient(to bottom, rgba(255,255,255,0.15) 0%, rgba(0,0,0,0.15) 100%), radial-gradient(at top center, rgba(255,255,255,0.40) 0%, rgba(0,0,0,0.40) 120%) #989898;
 background-blend-mode: multiply,multiply;
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
  background-image: linear-gradient(to top, lightgrey 0%, lightgrey 1%, #e0e0e0 26%, #efefef 48%, #d9d9d9 75%, #bcbcbc 100%);

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

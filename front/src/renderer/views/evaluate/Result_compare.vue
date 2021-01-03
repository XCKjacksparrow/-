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
     
          <h2 class="titleOne">同类实验成绩分析</h2>
           <el-select v-model="value1" multiple placeholder="请选择">
    <el-option
      v-for="item in options1"
      :key="item.value"
      :label="item.label"
      :value="item.value">
    </el-option>
  </el-select>

  <el-select
    v-model="value2"
    multiple
    collapse-tags
    style="margin-left: 20px;"
    placeholder="请选择">
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value">
    </el-option>
     </el-select>
    <el-select
    v-model="value1"
    multiple
    collapse-tags
    style="margin-left: 20px;"
    placeholder="请选择">
    <el-option
      v-for="item in options2"
      :key="item.value"
      :label="item.label"
      :value="item.value">
    </el-option>
  </el-select>
  <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">
  开始评价
</el-button>
<el-drawer
  title="教师评价"
  :visible.sync="drawer"
  :direction="direction"
  :before-close="handleClose">
  <div style="padding-left:20px;">
    <div class="innerDescription" style="font-weight:bold">姓名：</div><div class="innerDescription">潘丽</div> <br>
    <div class="innerDescription" style="font-weight:bold">所属学校：</div><div class="innerDescription">新星中学</div> <br>
    <div class="innerDescription" style="font-weight:bold">评分：</div><div class="innerDescription"><el-rate
  v-model="value5"
  disabled
  show-score
  text-color="#ff9900"
  score-template="{value}">
</el-rate></div> <br>
    <div class="innerDescription" style="font-weight:bold">学生的平均分：</div><div class="innerDescription">88</div> <br>
    <div class="innerDescription" style="font-weight:bold">学生的方差：</div><div class="innerDescription">5.5</div><br>
    <div class="innerDescription" style="font-weight:bold">学生的评价：</div><div class="innerDescription"><el-rate
  v-model="value5"
  disabled
  show-score
  text-color="#ff9900"
  score-template="{value}">
</el-rate></div> <br>
    <div class="innerDescription" style="font-weight:bold">校内对比：</div><div class="innerDescription">所带班级成绩相对稳定，方差较小，平均分较高，现阶段以指导难题为主。</div> <br>
    <div class="innerDescription" style="font-weight:bold">学校间对比：</div><div class="innerDescription">所带班级在同类学校之中处于上游，但与重点中学仍有较大差距。</div> 
   </div>
</el-drawer>
          <div>
            <div class="compareTitle" style="margin-left:250px;">新星中学</div>
            <div class="compareTitle" style="margin-left:500px;">泗洪中学</div>
          </div>
          <br />
          <div class="chartDisplay" id="parallelClassByGradesNormal" :style="chartDisplayCss"></div>
          <div class="chartDisplay" id="parallelClassByGradesPrior" :style="chartDisplayCss"></div>

          <h2 class="titleOne">同类实验知识点分析</h2>
          <div class="chartDisplay" id="parallelClassByKnowledgesNormal" :style="chartDisplayCss"></div>
          <div class="chartDisplay" id="parallelClassByKnowledgesPrior" :style="chartDisplayCss"></div>

          <h2 class="titleOne">分析建议</h2>
          <div>
            <el-table :data="tableData" border style="width: 100%;border-radius:5px;">
              <el-table-column prop="compare" label="比较" width="330"></el-table-column>
              <el-table-column prop="Normal" label="新星中学" width="450"></el-table-column>
              
            </el-table>
          </div>
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
      value5: 5,
      tableData: [{
        compare: '成绩评价',
        Normal: '不同班级之间成绩起伏较大，5班表现相对较好，其他班级不及格人数较多'

      },
      {
        compare: '知识点评价',
        Normal: '不同班级之间成绩起伏较大，5班表现相对较好，基础知识点较差'

      },
      {
        compare: '指导推荐',
        Normal: '其他班级以基础知识点为主，降低出错率，5班以部分缺陷知识为主'

      }
      ],
      msg: 'Welcome to Your Vue.js App',
      chartDisplayCss: {
        width: '550px',
        height: '300px',
        display: 'inline-block',
        margin: '30px'
      },
      options2: [
        {
          value: '选项1',
          label: '新星中学'
        },
        {
          value: '选项2',
          label: '沭阳中学'
        },
        {
          value: '选项3',
          label: '泗洪中学'
        },
        {
          value: '选项4',
          label: '淮北中学'
        },
        {
          value: '选项5',
          label: '洪祥中学'
        }

      ],
      value1: '选项1',
      drawer: false,
      direction: 'rtl',
      options1: [{
        value: '选项1',
        label: '江苏省'
      }, {
        value: '选项2',
        label: '北京'
      }, {
        value: '选项3',
        label: '上海'
      }, {
        value: '选项4',
        label: '湖北'
      }, {
        value: '选项5',
        label: '河南'
      }],
      value1: ['选项1'],
      value2: [],
      options: [
        {
          value: '选项1',
          label: '宿迁市'
        },
        {
          value: '选项2',
          label: '南京市'
        },
        {
          value: '选项3',
          label: '无锡市'
        },
        {
          value: '选项4',
          label: '徐州市'
        },
        {
          value: '选项5',
          label: '南通市'
        }
      ],
      value: ['选项1']
    }
  },
  mounted() {
    this.drawLine()
  },
  methods: {
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      // 按知识点得分汇总

      // 平行班成绩对比
      // 普通班级
      const parallelClassByGradesNormal = echarts.init(
        document.getElementById('parallelClassByGradesNormal')
      )
      // 重点中学
      const parallelClassByGradesPrior = echarts.init(
        document.getElementById('parallelClassByGradesPrior')
      )

      // 平行班知识点对比
      // 普通中学
      const parallelClassByKnowledgesNormal = echarts.init(
        document.getElementById('parallelClassByKnowledgesNormal')
      )
      // 重点中学
      const parallelClassByKnowledgesPrior = echarts.init(
        document.getElementById('parallelClassByKnowledgesPrior')
      )

      parallelClassByGradesNormal.setOption({
        title: {
          text: '各班级平均分'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['一班', '二班', '三班', '四班', '五班']
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
            '水的沸腾',
            '胆矾的研碎',
            '闻气体气味的方法',
            '液体的取用',
            '碳酸钠和盐酸反应',
            '测定空气中氧气的含量',
            '电解水'
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
            name: '一班',
            type: 'line',
            data: [20, 32, 10, 13, 90, 30, 20]
          },
          {
            name: '二班',
            type: 'line',
            data: [22, 82, 91, 24, 29, 33, 31]
          },
          {
            name: '三班',
            type: 'line',
            data: [15, 23, 20, 54, 90, 30, 40]
          },
          {
            name: '四班',
            type: 'line',
            data: [32, 32, 30, 54, 60, 70, 80]
          },
          {
            name: '五班',
            type: 'line',
            data: [82, 93, 90, 94, 90, 80, 88]
          }
        ]
      })
      parallelClassByGradesPrior.setOption({
        title: {
          text: '各班级平均分'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['一班', '二班', '三班', '四班', '五班']
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
            '水的沸腾',
            '胆矾的研碎',
            '闻气体气味的方法',
            '液体的取用',
            '碳酸钠和盐酸反应',
            '测定空气中氧气的含量',
            '电解水'
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
            name: '一班',
            type: 'line',
            data: [90, 92, 90, 93, 75, 85, 80]
          },
          {
            name: '二班',
            type: 'line',
            data: [82, 82, 91, 74, 79, 93, 91]
          },
          {
            name: '三班',
            type: 'line',
            data: [85, 83, 90, 94, 80, 90, 80]
          },
          {
            name: '四班',
            type: 'line',
            data: [82, 92, 90, 84, 80, 88, 88]
          },
          {
            name: '五班',
            type: 'line',
            data: [82, 93, 90, 94, 90, 80, 88]
          }
        ]
      })

      parallelClassByKnowledgesNormal.setOption({
        angleAxis: {
          type: 'category',
          data: ['一班', '二班', '三班', '四班', '五班']
        },
        radiusAxis: {},
        polar: {},
        series: [
          {
            type: 'bar',
            data: [1, 2, 3, 4, 4],
            coordinateSystem: 'polar',
            name: '氧气的制作',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [2, 4, 6, 1, 4],
            coordinateSystem: 'polar',
            name: '固体药品的取用',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [1, 2, 3, 4, 4],
            coordinateSystem: 'polar',
            name: '物质的加热',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [1, 2, 3, 4, 4],
            coordinateSystem: 'polar',
            name: '溶液的配制',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [1, 2, 3, 4, 4],
            coordinateSystem: 'polar',
            name: '蒸发与结晶',
            stack: 'a'
          }
        ],
        legend: {
          name: 'A',
          show: true,
          data: [
            '氧气的制作',
            '固体药品的取用',
            '物质的加热',
            '溶液的配制',
            '蒸发与结晶'
          ]
        }
      })
      parallelClassByKnowledgesPrior.setOption({
        angleAxis: {
          type: 'category',
          data: ['一班', '二班', '三班', '四班', '五班']
        },
        radiusAxis: {},
        polar: {},
        series: [
          {
            type: 'bar',
            data: [5, 3, 5, 5, 4],
            coordinateSystem: 'polar',
            name: '氧气的制作',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [5, 5, 5, 4, 5],
            coordinateSystem: 'polar',
            name: '固体药品的取用',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [5, 5, 5, 5, 5],
            coordinateSystem: 'polar',
            name: '物质的加热',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [5, 3, 5, 5, 5],
            coordinateSystem: 'polar',
            name: '溶液的配制',
            stack: 'a'
          },
          {
            type: 'bar',
            data: [5, 5, 5, 4, 5],
            coordinateSystem: 'polar',
            name: '蒸发与结晶',
            stack: 'a'
          }
        ],
        legend: {
          name: 'A',
          show: true,
          data: [
            '氧气的制作',
            '固体药品的取用',
            '物质的加热',
            '溶液的配制',
            '蒸发与结晶'
          ]
        }
      })
    }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.box-card {
  background-image: linear-gradient(to top, lightgrey 0%, lightgrey 1%, #e0e0e0 26%, #efefef 48%, #d9d9d9 75%, #bcbcbc 100%);

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
  background: linear-gradient(to bottom, rgba(255,255,255,0.15) 0%, rgba(0,0,0,0.15) 100%), radial-gradient(at top center, rgba(255,255,255,0.40) 0%, rgba(0,0,0,0.40) 120%) #989898;
 background-blend-mode: multiply,multiply;
  border: 0;
}

.clearfix > h2 {
  text-align: center;
}
.chartDisplay {
  // margin-left: 30px;
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
.compareTitle {
  color: white;
  font-size: 1.5em;
  display: inline-block;
}
.innerDescription{display: inline-block;
    font-size: 1.3rem;
    margin-bottom: 10px;
}
</style>

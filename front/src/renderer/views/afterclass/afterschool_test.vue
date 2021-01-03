<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <!-- title -->
        <div class="clearfix title">
          <h2>课后测试</h2>
        </div>
        <!-- charts display area -->
        <!-- <el-card class="chartsDisArea" shadow="never">
        
          <div class="classStatics">
           
          </div> -->

        <!-- </el-card> -->
        <el-row type="flex" class="row-bg" justify="center" style="min-height:700px"></el-row>
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
    return {}
  },
  mounted() {},
  methods: {
    drawLine() {}
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
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
  height: 100vh;
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
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.chartsDisArea {
  margin-bottom: 10px;

  border: 0;
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
</style>

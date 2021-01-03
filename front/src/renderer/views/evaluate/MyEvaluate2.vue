<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <!-- title -->
        <div class="clearfix title">
          <h2>学生反馈</h2>
        </div>

        <!--
            选择框
        -->
        <el-select v-model="value" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
        </el-select>

        <!--
            表格
        -->
            <el-table
                   :data="tableData"
                   style="width: 100%; margin-top:15px;border-radius:5px;"
                   :row-class-name="tableRowClassName">
                     <el-table-column
                          prop="date"
                          label="日期"
                          width="180">
                     </el-table-column>
                     <el-table-column
                          prop="name"
                          label="实验名称"
                          width="180">
                    </el-table-column>
                    <el-table-column
                          prop="address"
                          label="反馈情况">
                   </el-table-column>
             </el-table>
        <el-row type="flex" class="row-bg" justify="center" style="min-height:700px;"></el-row>
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
export default {
  name: 'hello',
  data() {
    return {
      msg: 'Welcome to Your Vue.js App',
      chartDisplayCss: {
        width: '300px',
        height: '300px',
        display: 'inline-block',
        margin: '30px'
      },
      options: [{
        value: '选项1',
        label: '酒精灯的使用'
      }, {
        value: '选项2',
        label: '氧气的制作'
      }, {
        value: '选项3',
        label: '二氧化碳的制作'
      }],
      tableData: [{
        date: '2016-05-02',
        name: '酒精灯的使用',
        address: '老师讲解非常到位'
      }, {
        date: '2016-05-04',
        name: '酒精灯的使用',
        address: '老师讲解非常到位'
      }, {
        date: '2016-05-01',
        name: '酒精灯的使用',
        address: '老师讲解非常到位'
      }, {
        date: '2016-05-03',
        name: '酒精灯的使用',
        address: '老师讲解非常到位'
      }],
      value: '选项1'
    }
  },
  mounted() {

  },
  methods: {
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row'
      } else if (rowIndex === 3) {
        return 'success-row'
      }
      return ''
    }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
  .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
// .title {
//   background: #2b3343 !important;
//   border: 1px solid black;
//   border-radius: 15px;
//   margin-bottom: 20px;
// }
// .title h2 {
//   color: white;
// }
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

// .box-card {
//   background-color: rgb(34, 41, 55) !important;
//   border: 0;
// }
.title {
  // background: #ffffff !important;
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
  height: 100vh;
  width: 100%;
}
.clearfix > h2 {
  text-align: center;
}

</style>

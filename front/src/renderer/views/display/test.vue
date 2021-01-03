<template>
  <div>
    <div class="dashboard-container">
      <el-card class="box-card" shadow="never">
        <!-- title -->
        <div class="clearfix title">
          <h2>课前测试</h2>
        </div>
              <div style="width: 900px; height: 100%">
        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="name" label="姓名" width="180">
          </el-table-column>
          <el-table-column prop="isSign" label="是否签到" width="180">
          </el-table-column>
          <el-table-column prop="signInTime" label="签到时间" >
          </el-table-column>
        </el-table>
      </div>

         
       
        <el-row type="flex" class="row-bg" justify="center" style="min-height:700px"></el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getResourceList } from '../../api/signIn'
import { mapGetters } from 'vuex'

const echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/pie')
require('echarts-gl')
export default {
  name: 'hello',
  data() {
    return {
      tableData: [],

    }
  },
  mounted() {

this.loadData()
  
  },
  methods: {
 
      loadData(){
              getResourceList().then((response) => {
        
        this.tableData = response
        console.log(response);
     
        


      })
      }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.dashbord {
  display: grid;
  grid-template-columns: 30% 70%;
  height: 100vh;
  width: 1739px;
  /* background: #eeeeee; */
  background-image: linear-gradient(
    to top,
    lightgrey 0%,
    lightgrey 1%,
    #e0e0e0 26%,
    #efefef 48%,
    #d9d9d9 75%,
    #bcbcbc 100%
  );
  grid-gap: 20px;
  padding: 20px;
}

.dashbord-middle {
  display: grid;
  grid-template-columns: 100%;
  grid-template-rows: 70% 30%;
}
.dashbord-middle .card {
  height: 100%;
}

.dashbord-footer {
  display: grid;
  grid-template-rows: 100%;
  grid-template-columns: 25% 25% 25% 25%;
}
.charts {
  width: 100%;
}
#main,
#main2,
#main3,
#main4,
#earth {
  height: 100%;
  width: 100%;
}

</style>


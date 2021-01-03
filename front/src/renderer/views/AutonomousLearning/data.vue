<template>
  <div>
    <!-- 网校课程 开始 -->
    <el-card class="box-card">
      <div class="clearfix title">
        <h2>热门课程</h2>
      </div>
      <div>
        <el-row>
          <el-col :span="4" v-for="edu in edulist" :key="edu.id" :offset="1">
            <el-card
              :body-style="{ padding: '0px', height: '360px' }"
              shadow="hover"
              style="width: 260px; height: 260px"
            >
              <div style="padding: 6px; height: 310px">
                <div>
                  <div
                    style="position: relative; top: 15px; text-align: center"
                  >
                    {{ edu.title }}
                  </div>
                </div>
                <div style="position: relative; top: 30px">
                  <el-image :src="edu.cover" fit="fit" style="height:138px;width:250px"></el-image>
                  <div style="position: relative; top: 10px; left: 66px">
                  </div>
                </div>
                <div style="position: relative; top: 45px">
                  &nbsp;&nbsp;<i class="el-icon-view"></i><span>9999</span>
                  <el-button type="text" style="float: right"
                    ><font size="4" @click="startstudy">开始学习</font></el-button
                  >
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>
import { getlist } from '../../api/index'

export default {
  data() {
    return {
      edulist: []
    }
  },

  created() {
    this.getclassteacher()
  },
  methods: {
    startstudy(id) {
      this.$router.push({ path: '/study/test/' + id })
    },
    getclassteacher() {
      getlist().then((response) => {
        this.edulist = response.data.eduList
      })
    }
  }
}
</script>
<style scoped>
.clearfix > h2 {
  text-align: center;
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
.title {
  background-image: linear-gradient(to right, #74ebd5 0%, #9face6 100%);
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}
</style>
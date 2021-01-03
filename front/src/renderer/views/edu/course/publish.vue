<template>
        <el-card class="box-card" >
        <div class="clearfix title">
          <h2>发布新课程</h2>
        </div>
        <el-steps :active="3" process-status="wait" align-center style="margin-bottom:40px;">
        <el-step title="填写课程基本信息"/>
        <el-step title="创建课程大纲"/>
        <el-step title="发布课程"/>
        </el-steps><div class="ccInfo">
        <img :src="coursePublish.cover">
                <div class="main">
                    <h2>{{ coursePublish.title }}</h2>
                    
                </div>
        </div>
            <div>
                <el-button @click="previous">返回修改</el-button>
                <el-button :disabled="saveBtnDisabled" type="primary" @click="publish">发布课程</el-button>
            </div>
        </el-card>
</template>
<script>
import { getPublishCourseInfo, PublishCourseInfo } from '../../../api/course'
export default {
  data() {
    return {
      coursePublish: {},
      courseId: '',
      saveBtnDisabled: false // 保存按钮是否禁用
    }
  },
  created() {
    console.log('publish created')
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id
      this.getCoursePublishVoById()
    }
  },
  methods: {
    getCoursePublishVoById() {
      getPublishCourseInfo(this.courseId)
        .then(response => {
          this.coursePublish = response.data.item
        })
    },
    previous() {
      console.log('previous')
      this.$router.push({ path: '/Course/chapter/' + this.courseId })
    },
    publish() {
      this.$confirm('此操作将发布课堂, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        PublishCourseInfo(this.courseId)
          .then((response) => {
            console.log(response)
            // 提示信息
            this.$message({
              type: 'success',
              message: '课堂发布成功!'
            })
            this.$router.push({ path: '/' })
          })
          .catch((error) => {
            this.$message({
              type: 'success',
              message: '课堂发布失败!'
            })
            console.log(error)
          })
      })
    }
  }
}
</script>

<style scoped>
.ccInfo {
background: #f5f5f5;
padding: 20px;
overflow: hidden;
border: 1px dashed #DDD;
margin-bottom: 40px;
position: relative;
}
.ccInfo img {
background: #d6d6d6;
width: 500px;
height: 278px;
display: block;
float: left;
border: none;
}
.ccInfo .main {margin-left: 520px;
}
.ccInfo .main h2 {
font-size: 28px;
margin-bottom: 30px;
line-height: 1;
font-weight: normal;
}
.ccInfo .main p {
margin-bottom: 10px;
word-wrap: break-word;
line-height: 24px;
max-height: 48px;
overflow: hidden;
}
.ccInfo .main p {
margin-bottom: 10px;
word-wrap: break-word;
line-height: 24px;
max-height: 48px;
overflow: hidden;
}
.ccInfo .main h3 {
left: 540px;
bottom: 20px;
line-height: 1;
font-size: 28px;
color: #d32f24;
font-weight: normal;
position: absolute;
}




.title {
  background-image: linear-gradient(to right, #74ebd5 0%, #9face6 100%);
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}
.title h2 {
  color: rgb(0, 0, 0);
}
.clearfix > h2 {
  text-align: center;
}
.box-card {
  background: #c9ccd3;
  background-image: linear-gradient(
    -180deg,
    rgba(255, 255, 255, 0.5) 0%,
    rgba(0, 0, 0, 0.5) 100%
  );
  background-blend-mode: lighten;
  border: 0;
  height: 100vh;
  width: 100%;
}
div /deep/ .el-step__title.is-wait {
  color: black;
}
</style>
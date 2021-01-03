<template>
  <!-- <h2 style="text-align: center;">发布新课程</h2> -->
  <el-card class="box-card">
    <div class="clearfix title">
      <h2>发布新课程</h2>
    </div>
    <el-steps
      :active="1"
      process-status="wait"
      align-center
      style="margin-bottom: 40px"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="提交审核" />
    </el-steps>
    <el-form label-width="120px" class="course-form">
      <el-form-item label="课程名称" prop="name">
        <el-input
          v-model="courseInfo.title"
          placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"
        />
      </el-form-item>
      <el-form-item label="课程ID" prop="id">
        <el-input v-model="courseInfo.id" placeholder="0，1，2，3，4" />
      </el-form-item>
      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API + '/eduoss/fileoss'"
          class="avatar-uploader"
          drag
          :limit="1"
          :on-exceed="onExceed"
          :file-list="fileList"
          list-type="picture"
          v-if="fileUrl == '' || fileUrl == null"
        >
          <i class="el-icon-upload"></i>
          <!-- <img :src="courseInfo.cover" style="width: 300px; height: 300px" /> -->
          <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            只能上传jpg文件，且不超过2MB
          </div>
        </el-upload>
        <el-image
          v-else
          :src="fileUrl"
          style="width: 300px; height: 300px"
          fit="fit"
          :preview-src-list="fileUrl"
        ></el-image>
      </el-form-item>
      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="save"
          >保存并下一步</el-button
        >
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { addCourse } from '../../../api/course'
export default {
  data() {
    return {
      // fileList:[],
      fileUrl: '',
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {
        title: '',
        id: '',
        cover: '/static/02.jpg'
      },

      saveBtnDisabled: false, // 保存按钮是否禁用
      courseId: '',

      BASE_API: process.env.BASE_API, // 接口API地址

      saveBtnDisabled: false // 保存按钮是否禁用
    }
  },
  created() {
    console.log('info created')
    this.fileUrl = ''
  },
  methods: {
    onExceed() {
      this.$message.error('封面只能上传1张图片！')
    },
    // 上传封面成功调用的方法
    handleAvatarSuccess(res, file) {
      console.log(file.name)
      console.log(res.data.url)
      // this.fileList.push({name:file.name, url: res.data.url})
      // console.log(this.fileList);
      this.fileUrl = res.data.url
      this.courseInfo.cover = res.data.url
    },
    // 上传之前调用的方法
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },

    save() {
      addCourse(this.courseInfo)
        .then((response) => {
          this.$router.push({ path: '/study/chapter/' + this.courseInfo.id })
        })
        .catch((error) => {
          console.log(error)
        })
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
.title h2 {
  color: rgb(0, 0, 0);
}
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
  background-blend-mode: lighten;
  border: 0;
  height: 100vh;
  width: 100%;
}
.el-input {
  width: 500px;
}

div /deep/ .el-step__title.is-wait {
  color: black;
}
</style>
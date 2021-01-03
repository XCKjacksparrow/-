<template>
  <div>
    <el-card class="box-card">
            <div class="clearfix title">
        <h2>名单导入</h2>
      </div>
      <el-form label-width="120px">
        <el-form-item label="信息描述">
          <el-tag type="info">excel模版说明</el-tag>
          <el-tag>
            <i class="el-icon-download" />
            <a
              :href="
                OSS_PATH +
                '/excel/%E8%AF%BE%E7%A8%8B%E5%88%86%E7%B1%BB%E5%88%97%E8%A1%A8%E6%A8%A1%E6%9D%BF.xls'
              "
              >点击下载模版</a
            >
          </el-tag>
        </el-form-item>
        <el-form-item label="选择Excel">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError"
            :disabled="importBtnDisabled"
            :limit="1"
            :action="BASE_API + '/user/addSubject'"
            name="file"
            accept="application/vnd.ms-excel"
          >
            <el-button slot="trigger" size="small" type="primary"
              >选取文件</el-button
            >
            <el-button
              :loading="loading"
              style="margin-left: 10px"
              size="small"
              type="success"
              @click="submitUpload"
              >{{ fileUploadBtnText }}</el-button
            >
          </el-upload>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      BASE_API: process.env.BASE_API, // 接口API地址
      OSS_PATH: process.env.OSS_PATH, // 阿里云OSS地址
      fileUploadBtnText: '上传到服务器', // 按钮文字
      importBtnDisabled: false, // 按钮是否禁用,
      loading: false
    }
  },

  methods: {
    submitUpload() {
      this.fileUploadBtnText = '正在上传'
      this.importBtnDisabled = true
      this.loading = true
      this.$refs.upload.submit()
    },
    fileUploadSuccess(response) {
      this.fileUploadBtnText = '导入成功'
      this.loading = false
    },
    fileUploadError(response) {
      this.fileUploadBtnText = '导入失败'
      this.loading = false
      this.$message({
        type: 'error',
        message: '导入失败'
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
<template>
    <div class="dashboard-container">
        <el-card class="box-card">
            <div  class="clearfix title">
                <h2>发布通知</h2>
            </div>
            <el-row style="background-image:linear-gradient(to top, #e6e9f0 0%, #eef1f5 100%);border: 10px solid #e9e9e9;border-radius: 15px">
                <el-form ref="form" :model="form"   label-position="left" style="border-radius: 15px">
                    <el-form-item label="通知标题">
                        <el-input  v-model="form.title"></el-input>
                    </el-form-item>
                    <el-form-item label="通知内容">
                        <el-input
                                type="textarea"
                                :autosize="{ minRows: 3, maxRows: 6}"
                                placeholder="请输入内容"
                                v-model="form.content">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="send">立即发布</el-button>
                    </el-form-item>
                </el-form>
            </el-row>
        </el-card>

    </div>
</template>

<script>
  export default {
    data() {
      return {
        form: {
          title: '',
          content: '',
          type: '课堂通知'
        }
      }
    },
    methods: {
      send() {
        if (this.form.title !== '' && this.form.content !== '') {
          this.socket.sendMessage(this.form, 'release')

          this.$message({ type: 'success', message: '发布成功' })
          this.$store.dispatch('push_release', this.form)
          this.form = {
            title: '',
            content: '',
            type: '课堂通知'
          }
        } else {
          this.$message({ type: 'warning', message: '请输入标题和内容' })
        }
      }
    },
    computed: {}
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .dashboard {
        &-container {
            // margin: 30px;
        }

        &-text {
            font-size: 30px;
            line-height: 46px;
        }
    }
    .clearfix>h2{
      text-align: center;
    }
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
</style>

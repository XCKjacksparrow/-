<template>
<el-card class="box-card" >
    <div class="clearfix title">
      <h2>发布新课程</h2>
    </div>
    <el-steps
      :active="2"
      process-status="wait"
      align-center
      style="margin-
                    bottom: 40px;"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>
    <el-button type="text" @click="opendialogChapterFormVisible()">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
      <li v-for="chapter in ChapterVideoList" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button type="text" @click="opendialogVideoFormVisible(chapter.id)">添加课时</el-button>
            <el-button style type="text" @click="Update(chapter.id)">编辑</el-button>
            <el-button type="text" @click="removeChapterById(chapter.id)">删除</el-button>
          </span>
        </p>
        <!-- 视频 -->
        <ul class="chanpterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>
              {{ video.title }}
              <span class="acts">
                <el-button type="text" @click="updateVideoById(video.id)">编辑</el-button>
                <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0" controls- position="right" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title" />
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right" />
        </el-form-item>

      
        <el-form-item label="上传视频">
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API+'/eduvod/video/addvideo'"
            :limit="1"
            class="upload-demo"
          >
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">
                最大支持1G，
                <br />支持3GP、ASF、AVI、DAT、DV、FLV、F4V、
                <br />GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、
                <br />MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、
                <br />SWF、TS、VOB、WMV、WEBM 等视频格式上传
              </div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>
</el-card>
</template>
<script>
import { addChapter, GetChapterVideo } from '../../../api/chapter'

import { addVideo } from '../../../api/Video'
export default {
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      dialogVideoFormVisible: false,
      ChapterVideoList: [],
      courseId: '',
      dialogChapterFormVisible: false,
      chapter: {
        // 章节对象
        title: '',
        sort: 0,
        id: ''
      },
      video: {
        // 课时对象
        title: '',
        sort: 0,
        videoSourceId: '',
        videoOriginalName: ''
      },
      fileList: [], // 上传文件列表
      BASE_API: process.env.BASE_API // 接口API地址
    }
  },
  created() {
    console.log('chapter created')
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id
    }
    this.getChapterVideoListById()
  },
  methods: {
    handleVodRemove(file, fileList) {},
    beforeVodRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleVodUploadSuccess(response, file, fileList) {
      this.video.videoSourceId = response.data.videoId
      this.video.videoOriginalName = file.name
    },

    handleUploadExceed() {},

    removeVideo(id) {},

    addVideo() {
      this.video.courseId = this.courseId

      addVideo(this.video).then((response) => {
        this.$message({
          type: 'success',
          message: '添加小节信息成功!'
        })
        this.getChapterVideoListById()
      })
    },

    updateVideoById(id) {},
    update() {},

    saveOrUpdateVideo() {
      this.dialogVideoFormVisible = false
      if (!this.video.id) {
        this.addVideo()
      } else {
        console.log(444)
        this.update()
      }
    },
    opendialogVideoFormVisible(chapterId) {
      this.video = {
        // 课时对象
        title1: '',
        sort: 0,
        free: 0,
        videoSourceId: ''
      }
      this.fileList = []
      this.video.videoSourceId = ''
      this.video.videoOriginalName = ''
      this.dialogVideoFormVisible = true
      this.video.chapterId = chapterId
    },

    opendialogChapterFormVisible() {
      this.dialogChapterFormVisible = true
      this.chapter.title = ''
      this.chapter.sort = ''
    },

    Update(id) {},

    getChapter(id) {},
    addChapter() {
      console.log('tianjiachengg')
      this.dialogChapterFormVisible = false
      this.chapter.courseId = this.courseId
      addChapter(this.chapter).then((response) => {
        console.log(response)
        this.$message({
          type: 'success',
          message: '添加章节信息成功!'
        })
        this.getChapterVideoListById()
      })
    },
    updateChapter() {
      this.dialogChapterFormVisible = false
      Chapter.updateChapter(this.chapter).then((response) => {
        this.$message({
          type: 'success',
          message: '修改章节信息成功!'
        })
        this.getChapterVideoListById()
      })
    },

    saveOrUpdate() {
      if (!this.chapter.id) {
        this.addChapter()
      } else {
        this.updateChapter()
      }
    },

    getChapterVideoListById() {
      GetChapterVideo(this.courseId).then((response) => {
        console.log(response.data.items)
        this.ChapterVideoList = response.data.items
      })
    },

    removeChapterById(id) {},

    previous() {
      console.log('previous')
      this.$router.push({ path: '/study/info/' + this.courseId })
    },
    next() {
      console.log('next')
      this.$router.push({ path: '/study/publish/' + this.courseId })
    }
  }
}
</script>

<style scoped>
.chanpterList {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}
.chanpterList li {
  position: relative;
}
.chanpterList p {
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #ddd;
}
.chanpterList .acts {
  float: right;
  font-size: 14px;
}
.videoList {
  padding-left: 50px;
}
.videoList p {
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #ddd;
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
<template>
  <div>
      <el-card class="box-card" shadow="never">
        <div class="clearfix title">
          <h2>预习资料</h2>
        </div>
        <el-card class="operations">
          <el-row v-if="roles[0] === 'teacher'">
            <el-upload
              class="upload-demo"
              ref="upload"
              :action="`api/upload/uploadFile`"
              :file-list="fileList"
              :on-success="onUploadSuccess"
              :auto-upload="false"
            >
              <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
              <el-button
                style="margin-left: 10px;"
                size="small"
                type="success"
                @click="submitUpload"
              >上传到服务器</el-button>
              <div slot="tip" class="el-upload__tip" style="color:black">上传文件</div>
            </el-upload>
          </el-row>
        </el-card>
        <el-row type="flex" class="row-bg table-buttom" justify="center" style="min-height:500px">
        <el-table 
                            ref="filterTable"
                            :data="tableData"
                            :fit="true"
                            style="width: 100%;border-radius: 5px">
                        <el-table-column 
                                prop="initialName"
                                label="资源名称"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="data"
                                label="资源说明"

                        >
                            <!-- <template slot-scope="slot">
                                <span style="color:lightgrey">暂无</span>
                            </template> -->

                        </el-table-column>

                        <el-table-column
                                prop="type"
                                label="文件类型"

                        >
                            <!-- <template slot-scope="slot">
                                <el-tag type="primary">
                                    {{slot.row.type.split('.')[1]}}
                                </el-tag>
                            </template> -->
                        </el-table-column>
                        <el-table-column
                                fixed="right"
                                label="操作"
                        >
                            <template slot-scope="scope">
                                <el-button @click="downloadToMe(scope.row)" size="mini">下载</el-button>
                                <!-- <el-button @click="distribute(scope.row)" type="primary" size="mini" v-if="roles[0]==='teacher'&&students.length>0">
                                    分发
                                </el-button> -->
                                <el-button type="danger" size="mini" v-if="roles[0]==='teacher'"
                                           @click="deleteFile(scope.row.id)">删除
                                </el-button>
                                <el-button type="success" size="mini" @click="parse(scope.row)"
                                           v-if="scope.row.type==='.json'&&roles[0]==='teacher'">解析
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
        </el-row>
      </el-card>
    </div>

</template>

<script>
import { mapGetters } from 'vuex'
import { getResourceList, deleteResource, downloadFile } from '../../api/table'
const ipcRenderer = require('electron').ipcRenderer
export default {
  data() {
    return {
      fileList: [],
      tableData: [{ initialName: '初中化学', data: '课堂ppt', type: 'zip' }],
      folder: ''
    }
  },
  mounted() {
    // this.getClassFile()
  },
  methods: {

    tableRowClassName({ row, rowIndex }) {
      if (row.grade < 60) {
        return 'warning-row'
      } else if (row.grade === '未操作') {
        return 'grey-row'
      } else {
        return 'success-row'
      }
    },
    getClassFile() {
      getResourceList()
        .then((res) => {
          console.log(res)
          this.tableData = res.data
        })
        .catch((err) => {
          this.$message.error(err.toString())
        })
        .finally(() => {})
    },
    onUploadSuccess(response, file, fileList) {
      console.log(response)
      if (response.code === 200) {
        console.log(file)
        this.$message({ type: 'success', message: '上传成功' })
        this.getClassFile()
        this.fileList = []
      } else {
        this.$message.error(response.msg)
      }
    },
    submitUpload() {
      this.$refs.upload.submit()
    },
    deleteFile(id) {
      const that = this
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteResource(id)
          .then((res) => {
            if (res.code === 200) {
              this.$message({ type: 'success', message: '操作成功' })
              that.getClassFile()
              const data = null
              this.socket.sendMessage(data, 'updataClassFile')
            } else {
              this.$message.error(res.msg)
            }
          })
          .catch((err) => {
            this.$message.error('操作失败')
            console.log(err)
          })
      })
    },
    downloadToMe(row) {
      const path = row.address

      const folder = 'D:\\expTemp'
      console.log(folder + '  ' + path)
      this.downloadFile(path, folder)
    },
    // 下载文件
    downloadFile(path, folder) {
      const that = this
      this.folder = folder
      const downloadAddress = `http://${this.serverIp}:${this.serverPort}${path}`
      // console.log(downloadAddress)
      ipcRenderer.send('download', downloadAddress + '+' + folder)
      this.$message({
        type: 'success',
        message: '成功下载至文件夹：' + folder
      })
    },
    // 分发
    distribute(row) {
      const path = row.address
      const folder = this.downloadFilePath
      this.socket.sendMessage({ path, folder }, 'distribute')
      console.log('已经发送分发实验文件信息')
    },
    // 解析
    parse(row) {
      const fileName = row.uuidName
      console.log('收到解析' + fileName)
      downloadFile(fileName)
        .then((res) => {
          // console.log(res)
          const nodes = { ...res } // .nodeDataArray
          const expInfo = {}
          const steps = []
          const expTitle = ''
          console.log('解析数据输出')
          this.$store.dispatch('set_stepError', res)
          // Object.keys(nodes).map(key => {
          //   console.log(nodes[key].Suggestions)
          //   // 正式的流程
          //   // if (node.key > 0) {
          //   const stepId = key
          //   // const stepDes = node.text 标题字段，目前未使用
          //   const score = nodes[key].score
          //   steps.push({ stepId, score })
          //   // } else if (node.key <- 2) {
          //   // 实验介绍
          //   // expTitle = node.text
          //   // }
          //   expInfo = { steps, score } // 存入步骤和分数字段用于页面显示
          //   this.$store.dispatch('set_stepError', expInfo)
          // })
          // nodes.forEach(node => {
          //   if (node.key > 0) {

          //     // 正式的流程
          //     const stepId = node.key
          //     // const stepDes = node.text 标题字段，目前未使用
          //     const score = nodes[node.index].score
          //     steps.push({ stepId, score })
          //   } else if (node.key < -2) {
          //     // 实验介绍
          //     // expTitle = node.text
          //   }
          // })

          this.$message({ type: 'success', message: '解析成功' })
        })
        .catch((err) => {
          this.$message.error('解析失败')
          console.log(err)
        })
    }
  },
  computed: {
    ...mapGetters(['roles', 'students', 'stepError', 'release'])
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.clearfix > h2 {
  text-align: center;
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
.box-card {
  background-image: linear-gradient(to top, lightgrey 0%, lightgrey 1%, #e0e0e0 26%, #efefef 48%, #d9d9d9 75%, #bcbcbc 100%);
  border: 0;
  height: 100vh;
  width: 100%;
}
.operations{
  margin-bottom: 10px;
  background-image: linear-gradient(to top, #d5d4d0 0%, #d5d4d0 1%, #eeeeec 31%, #efeeec 75%, #e9e9e7 100%);
  color:white;
  border:1px solid #bcbcbc;
  border-radius: 5px;
}
.table-buttom {
  background-image: linear-gradient(to top, #d5d4d0 0%, #d5d4d0 1%, #eeeeec 31%, #efeeec 75%, #e9e9e7 100%)!important;
}
</style>

<template>
  <div class="dashboard-container">
    <el-card class="box-card" style="height:calc(100vh - 50px)">
      <el-row type="flex" class="row-bg" justify="center" style="height:calc(94vh - 50px)">
        <!-- 左边聊天框 包括input -->
        <el-container class="chat">
          <el-container class="right">
            <el-main class="main" id="main">
              <div v-for="item in Historys" :key="item.id">
                <el-avatar
                  style="float:left;position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份 -->
                <el-row style="padding:10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{item.userName}}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{item.content}}</el-tag>
                </el-row>
              </div>
              <div v-for="item in toAllHistory" :key="item.id">
                <el-avatar
                  style="float:left;position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding:10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{item.username}}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{item.message}}</el-tag>
                </el-row>
              </div>
              <div id="scroll" style="overflow: hidden"></div>
            </el-main>
            <el-footer class="footer">
              <el-input
                placeholder="请输入内容"
                v-model="message"
                @keydown.enter.native="sendChatToAll"
                style="width:800px"
              >
                <span slot="prepend">发送消息：</span>
                <el-button
                  title="点击发送消息"
                  style="background-color:#409EFF;font-size:16px;color:#FFFFFF"
                  slot="append"
                  @click="sendChatToAll"
                  icon="el-icon-s-promotion"
                ></el-button>
              </el-input>
            </el-footer>
          </el-container>

          <!-- 右边列表 -->
          <el-aside class="aside">
            <el-collapse
              style="border:1px solid black; border-radius:5px; background-color:#C0C0C0;"
            >
              <span style="color:lightgrey" v-if="students.length===0">暂无数据</span>
              <el-row v-for="item in students" :gutter="20" style="padding:10px" :key="item.id">
                <el-col :span="6">
                  <el-avatar
                    src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                  ></el-avatar>
                </el-col>
                <el-col :span="12" style="font-size:18px;margin-top: 5px">{{item.username}}</el-col>
                <el-col :span="6"></el-col>
              </el-row>
            </el-collapse>
          </el-aside>
        </el-container>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { mapGetters, mapSetters } from 'vuex'

import { getChatList, addChat } from '../../api/chat'

export default {
  data() {
    return {
      message: '',
      messageList: [],
      Historys: []
    }
  },
  mounted() {
    this.initMain()
  },
  methods: {
    initMain() {
      getChatList().then(response => {
        console.log(response)
        this.Historys = response
      })
    },
    sendChatToAll() {
      this.socket.sendMessageToAll(
        {
          message: this.message,
          date: new Date(),
          username: this.socket.username
        },
        'chatToAll'
      )
      // TODO:保存到自己的历史
      // console.log('this.socket.baseData')
      // console.log(this.socket.baseData)
      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: this.message,
        date: new Date(),
        username: this.socket.username,
        isMe: true
      }

      // 保存到后台
      addChat(
        this.socket.username,
        this.socket.baseData.fromIp,
        this.message,
        this.socket.baseData.fromIdentity
      ).then((response) => {})

      console.log('chatToAll :')
      console.log(record)
      this.message = ''
      this.$store.dispatch('push_toAll', record)
    },
    clearHistory() {
      this.$store.dispatch('reset_toAll')
    }
  },
  computed: {
    ...mapGetters(['students', 'teachers', 'toAllHistory'])
  },
  watch: {
    toAllHistory(newVal) {
      const e = document.getElementById('main')
      e.scrollTop = e.scrollHeight
    }
  }
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

.tag {
  margin-right: 10px;
}

.aside {
  padding: 10px;
  background-image: linear-gradient(60deg, #29323c 0%, #485563 100%);
  border: 1px solid black;
  border-radius: 5px;
  margin-left: 5px;
  color: white;
  height: 93vh;
}

.header {
  padding: 10px;
  margin-left: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
}

.chat {
  min-height: 550px;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.main {
  padding: 10px;
  // margin-right: 5px;
  color: #fff;
  border: 1px solid black;
  border-right: none;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  background-image: linear-gradient(60deg, #29323c 0%, #485563 100%);
}

.footer {
  padding: 1px;
  padding-top: 20px;
  width: 50vw;
}

.clearfix > h2 {
  text-align: center;
}
.el-tag {
  font-size: 16px;
  color: #222222 !important;
}
.footer .el-input-group__prepend {
  padding: 0 0 0 11px;
}
.footer span {
  font-size: 16px;
}
</style>
<style>
.el-input-group__prepend {
  background-color: #409eff !important;
  color: #ffffff;
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
.clearfix > h2 {
  text-align: center;
}
</style>
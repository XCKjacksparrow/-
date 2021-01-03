<template>
  <el-menu class="navbar" mode="horizontal" style="background: #2b3343">
    <hamburger
      class="hamburger-container"
      :toggleClick="toggleSideBar"
      :isActive="sidebar.opened"
    ></hamburger>
    <breadcrumb></breadcrumb>

    <!-- TODO 显示视频 -->
        <el-dropdown class="avatar-container" style="right: 250px">
            <el-button v-show="false">
                显示视频<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button
      >
            <el-dropdown-menu slot="dropdown">
                
        <div
          class="box-card onlineDisplay"
          style="width: 500px;  height: 400px"
        >
                    
          <div
            v-for="video in videoList"
            :key="video.id"
            style="display: inline-block"
          >
                        
            <div :id="video.userName" class="videoPattern"></div>
                      
          </div>
                  
        </div>
              </el-dropdown-menu
      >
          </el-dropdown
    >

    <!-- 视频控件 -->
        <el-dropdown class="avatar-container" style="right: 125px">
            <el-button>
                控制面板<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button
      >
            <el-dropdown-menu slot="dropdown">
                
        <div>
                    
          <div style="background-color: #2b3343 !important">
                        <el-button class="el-icon-microphone" type="success">{{
              speakingStatus
            }}</el-button>
                      
          </div>
                    
          <div
            style="
              width: 900px;
              height: 100%;
              background-color: #2b3343 !important;
            "
          >
                        <el-button
              type="primary"
              style="margin: 20px 0 5px 70px"
              @click="muteAll()"
              >全体静音</el-button
            >
                        <el-button
              type="primary"
              style="margin: 20px 0 5px 70px"
              @click="cancelMuteAll()"
              >解除静音</el-button
            >
            <el-button
              type="primary"
              style="margin: 20px 0 5px 70px"
              @click="opendrawer = true"
              id="openChatBtn"
              >师生交流</el-button
            >
                      
          </div>

                  
        </div>
              </el-dropdown-menu
      >
          </el-dropdown
    >

    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <img class="user-avatar" src="./header.jpg" />
        <i class="el-icon-caret-bottom"></i>
      </div>
      <el-dropdown-menu class="user-dropdown" slot="dropdown">
        <router-link class="inlineBlock" to="/">
          <el-dropdown-item>主页</el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span @click="logout" style="display: block">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <el-drawer
      title="消息通知"
      :visible.sync="opendrawer"
      :with-header="false"
      append-to-body:true
      size="90%"
    >
      <div style="width: 700px; height: 100%">
        <br />
        <br />
        <el-container class="chat">
          <el-container class="right">
            <el-main class="main chat" id="main" style="height: 100%">
              <div v-for="item in Historys" :key="item.id">
                <el-avatar
                  style="float: left; position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding: 10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{ item.userName }}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{ item.content }}</el-tag>
                </el-row>
              </div>
              <div v-for="item in toAllHistory" :key="item.id">
                <el-avatar
                  style="float: left; position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding: 10px">
                  <span v-if="item.fromIdentity == 'teacher'">【教师】</span>
                  {{ item.username }}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{ item.message }}</el-tag>
                </el-row>
              </div>
              <!-- <div id="scroll" style="overflow: hidden"></div> -->
            </el-main>
            <el-aside class="aside">
              <el-collapse
                style="
                  border: 1px solid black;
                  border-radius: 5px;
                  background-color: #c0c0c0;
                "
              >
                <span
                  style="color: lightgrey"
                  v-if="students.length === 0"
                  @click="drawer = true"
                  >暂无数据</span
                >
                <el-row
                  v-for="item in students"
                  :gutter="20"
                  style="padding: 10px"
                  :key="item.id"
                >
                  <el-col :span="6">
                    <el-avatar
                      src="https://cubfe.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                    ></el-avatar>
                  </el-col>

                  <el-col :span="9" style="font-size: 18px; margin-top: 5px">{{
                    item.username
                  }}</el-col>

                  <el-col :span="3">
                    <el-popover
                      placement="top-start"
                      width="50"
                      trigger="hover"
                      content="进入聊天"
                    >
                      <el-button
                        :id="item.username + 'chatEach'"
                        type="primary"
                        icon="el-icon-chat-dot-round"
                        @click="sendmessage(item)"
                        size="mini"
                        slot="reference"
                      ></el-button>
                    </el-popover>
                  </el-col>

                  <el-col :span="3">
                    <el-popover
                      placement="top-start"
                      width="50"
                      trigger="hover"
                      content="进入指导"
                    >
                      <el-button
                        type="primary"
                        icon="el-icon-chat-line-square"
                        @click="guide()"
                        size="mini"
                        slot="reference"
                      ></el-button>
                    </el-popover>
                  </el-col>
                </el-row>

                <el-row
                  v-for="item in teachers"
                  :gutter="20"
                  style="padding: 10px"
                  :key="item.id"
                >
                  <el-col :span="6">
                    <el-avatar
                      src="https://cubfe.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                    ></el-avatar>
                  </el-col>

                  <el-col :span="9" style="font-size: 18px; margin-top: 5px">{{
                    item.username
                  }}</el-col>

                  <el-col :span="3">
                    <el-popover
                      placement="top-start"
                      width="50"
                      trigger="hover"
                      content="进入聊天"
                    >
                      <el-button
                        :id="item.username + 'chatEach'"
                        type="primary"
                        icon="el-icon-chat-dot-round"
                        @click="sendmessage(item)"
                        size="mini"
                        slot="reference"
                      ></el-button>
                    </el-popover>
                  </el-col>

                </el-row>
              </el-collapse>
            </el-aside>
          </el-container>
        </el-container>
        <br />
        <br />
        <el-row>
          <el-col :span="10">
            <el-footer class="footer">
              <el-input
                placeholder="请输入内容"
                v-model="message"
                @keydown.enter.native="sendChatToAll"
                style="width: 400px"
              >
                <span slot="prepend">发送消息：</span>
                <el-button
                  title="点击发送消息"
                  style="
                    background-color: #409eff;
                    font-size: 16px;
                    color: #ffffff;
                  "
                  slot="append"
                  @click="sendChatToAll"
                  icon="el-icon-s-promotion"
                ></el-button>
              </el-input>
            </el-footer>
          </el-col>
        </el-row>
      </div>
    </el-drawer>

    <el-drawer
      title="聊天室"
      :visible.sync="opendrawer1"
      :with-header="false"
      size="50%"
    >
      <div style="width: 700px; height: 100%">
        <br />
        <br />
        <el-container class="chat">
          <el-container class="right">
            <el-main class="main chat" id="main" style="height: 100%">
              <div v-for="item in Historyseach" :key="item.id">
                <el-avatar
                  style="float: left; position: relative"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                ></el-avatar>
                <!-- 这里显示ip、身份                               -->
                <el-row style="padding: 10px">
                  <span v-if="item.fromIdentity1 == 'teacher'">【教师】</span>
                  {{ item.userName1 }}
                  <el-tag type="danger" v-if="item.isMe">我</el-tag>
                </el-row>
                <!--这里显示内容                               -->
                <el-row style="margin-left: 40px">
                  <el-tag type="info">{{ item.content }}</el-tag>
                </el-row>
              </div>

              <!-- <div id="scroll" style="overflow: hidden"></div> -->
            </el-main>
          </el-container>
        </el-container>
        <br />
        <br />
        <el-row>
          <el-col :span="10">
            <el-footer class="footer">
              <el-input
                placeholder="请输入内容"
                v-model="messageeach"
                @keydown.enter.native="sendChatToAll"
                style="width: 400px"
              >
                <span slot="prepend">发送消息：</span>
                <el-button
                  title="点击发送消息"
                  style="
                    background-color: #409eff;
                    font-size: 16px;
                    color: #ffffff;
                  "
                  slot="append"
                  @click="sendNotice()"
                  icon="el-icon-s-promotion"
                ></el-button>
              </el-input>
            </el-footer>
          </el-col>
        </el-row>
      </div>
    </el-drawer>
    <!-- <div>
      <el-dialog
        :visible.sync="dialogFlag"
        :fullscreen="true"
        :before-close="stopDisplay"
         v-if="addDialogVisible"

        center
      >
        <div>
          <Guide
            :dialogIp="targetIp"
            :dialogIdentity="targetIdentity"
            :studentName="username"
          />
        </div>
      </el-dialog>
    </div> -->
  </el-menu>
</template>

<script>
// import Guide from '../../monitor/Guide'
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import { getChatList, addChat } from "../../../api/chat";
import { getChatEachList, addChatEach } from "../../../api/chateach";

import { getuserlist, getuserlistById } from "../../../api/user";
// 声网 语音视频
import AgoraRtcEngine from "agora-electron-sdk";
import path from "path";
import os from "os";
const shell = require("electron").shell;
const APPID = process.env["AGORA_APPID"] || "a0bec9a017144787a4976b9102ff9667";

export default {
  //   components: {
  //   Guide
  // },
  data() {
    return {
      dialogFlag: false,
      drawer: false,
      opendrawer: false,
      opendrawer1: false,
      message: "",
      messageeach: "",
      messageList: [],
      Historys: [],
      Historyseach: [],
      videoList: [],
      uid: "",
      speakingStatus: "正在讲话",
      direction: "rtl",
      toIp: "",
      toIdentity: "",
      touserName: "",
      addDialogVisible: false,
      targetIp: "",
      targetIdentity: "",
      username: "",
    };
  },
  components: {
    Breadcrumb,
    Hamburger,
  },
  computed: {
    ...mapGetters(["sidebar", "avatar"]),
    ...mapGetters(["students", "teachers", "toAllHistory"]),
  },
  mounted() {
    this.init(), this.initMain();
  },
  methods: {
    guide() {
      this.opendrawer = false;
      this.$router.push({ path: "/chat/info" });

      //  console.log(5555)
      // console.log('进入实验指导' + ip + identity + username)
      // // this.$router.push(`/monitor/guide?ip=${ip}&identity=${identity}`)
      // this.targetIp = ip
      // this.targetIdentity = identity
      // this.username = username
      // console.log(this.targetIp)
      // // let guidWindow = new BrowserWindow({show: false})
      // // guidWindow.maximize()
      // // guidWindow.show()
      // // path.join(Guide)
      // // this.socket.sendMessageToOne({ message: value }, 'notice', ip, identity)
      // this.dialogFlag = true
      // this.addDialogVisible = true
    },
    stopDisplay() {
      // 停止学生端的单向流
      this.addDialogVisible = false;
      this.dialogFlag = false;
      this.$store.dispatch("reset_toOne", null);
    },
    sendNotice() {
      // 向学生发送消息提醒
      this.socket.sendMessageToOne(
        { message: this.messageeach },
        "notice",
        this.toIp,
        this.toIdentity
      );

      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: this.messageeach,
        date: new Date(),
        username: this.socket.username,
        isMe: true,
      };

      // 保存到后台
      addChatEach(
        this.socket.username, //userName1
        this.socket.baseData.fromIp, //ip1
        this.touserName,
        this.toIp,
        this.messageeach, //content
        this.socket.baseData.fromIdentity, //fromIdentity1
        this.toIdentity
      ).then((response) => {
        this.initMainEach();
      });

      console.log("chat to one  :");
      console.log(record);
      this.messageeach = "";

      // this.$store.dispatch('push_toAll', record)
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {});
    },

    sendmessage(item) {
      console.log("student is");
      console.log(item);
      this.opendrawer1 = true;
      this.toIp = item.ip;
      this.toIdentity = item.identity;
      this.touserName = item.username;
      this.initMainEach();
    },
    initMain() {
      getChatList().then((response) => {
        console.log(response);
        this.Historys = response;
      });
    },

    initMainEach() {
      getChatEachList(this.socket.baseData.fromIp, this.toIp).then(
        (response) => {
          this.Historyseach = response;
        }
      );
    },
    sendChatToAll() {
      this.socket.sendMessageToAll(
        {
          message: this.message,
          date: new Date(),
          username: this.socket.username,
        },
        "chatToAll"
      );
      // TODO:保存到自己的历史
      // console.log('this.socket.baseData')
      // console.log(this.socket.baseData)
      const record = {
        fromIp: this.socket.baseData.fromIp,
        fromIdentity: this.socket.baseData.fromIdentity,
        message: this.message,
        date: new Date(),
        username: this.socket.username,
        isMe: true,
      };

      // 保存到后台
      addChat(
        this.socket.username,
        this.socket.baseData.fromIp,
        this.message,
        this.socket.baseData.fromIdentity
      ).then((response) => {});

      console.log("chatToAll :");
      console.log(record);
      this.message = "";
      this.$store.dispatch("push_toAll", record);
    },
    clearHistory() {
      this.$store.dispatch("reset_toAll");
    },
    muteAll() {
      rtcEngine.muteAllRemoteAudioStreams(true);
    },
    cancelMuteAll() {
      rtcEngine.muteAllRemoteAudioStreams(false);
    },
    init() {
      getuserlist().then((response) => {
        this.videoList = response;
      });
      getuserlistById(this.socket.username).then((response) => {
        this.uid = response; // 初始化videos
        this.$nextTick(function () {
          if (global.rtcEngine) {
            global.rtcEngine.release();
            global.rtcEngine = null;
          }
          if (!APPID) {
            alert("Please provide APPID in App.vue");
            return;
          }
          const consoleContainer = document.querySelector("#console");
          const rtcEngine = new AgoraRtcEngine();
          rtcEngine.initialize(APPID); // listen to events
          rtcEngine.on("joinedChannel", (channel, uid, elapsed) => {
            consoleContainer.innerHTML = `join channel success ${channel} ${uid} ${elapsed}`;
            const localVideoContainer = document.querySelector(
              "#" + this.socket.username
            ); // setup render area for local user
            rtcEngine.setupLocalVideo(localVideoContainer);
          });
          rtcEngine.on("error", (err, msg) => {
            consoleContainer.innerHTML = `error: code ${err} - ${msg}`;
          });
          rtcEngine.on("userJoined", (uid) => {
            getuserlist().then((response) => {
              for (var i = 0; i < response.length; i++) {
                if (uid == response[i].id) {
                  // setup render area for joined user
                  const remoteVideoContainer = document.querySelector(
                    "#" + response[i].userName
                  );
                  rtcEngine.setupViewContentMode(uid, 1);
                  rtcEngine.subscribe(uid, remoteVideoContainer);
                  break;
                }
              }
            });
          });
          rtcEngine.enableAudioVolumeIndication(1250, 3, true);
          rtcEngine.on(
            "groupAudioVolumeIndication",
            (speakers, speakerNumber, totalVolume) => {
              if (speakers.length == 0) {
                this.speakingStatus = "";
              } else {
                // 选出分贝最大的那个uid 如果是0 表示自己
                var maxId = 0;
                for (var i = 1; i < speakers.length; i++) {
                  if (speakers[i].volume > speakers[maxId].volume) {
                    maxId = i;
                  }
                }
                if (speakers[maxId].uid == 0) {
                  this.speakingStatus = "我正在讲话";
                } else {
                  for (var j = 0; j < this.videoList.length; j++) {
                    if (this.videoList[j].id == speakers[maxId].uid) {
                      this.speakingStatus =
                        this.videoList[j].userName + "正在讲话";
                      break;
                    }
                  }
                }
              }
            }
          ); // set channel profile, 0: video call, 1: live broadcasting
          rtcEngine.setChannelProfile(1);
          rtcEngine.setClientRole(1); // enable video, call disableVideo() is you don't need video at all
          rtcEngine.disableVideo();
          rtcEngine.enableAudio();
          const logpath = path.join(os.homedir(), "agorasdk.log"); // set where log file should be put for problem diagnostic
          rtcEngine.setLogFile(logpath); // join channel to rock!
          rtcEngine.joinChannel(null, "demoChannel", null, this.uid);
          global.rtcEngine = rtcEngine;
        });
      });
    },

    toggleSideBar() {
      this.$store.dispatch("ToggleSideBar");
    },
    logout() {
      this.$store.dispatch("LogOut", { userip: this.localIp }).then(() => {
        location.reload(); // 为了重新实例化vue-router对象 避免bug
      });
      this.$router.push("/login");
    },
  },
  // watch: {
  // toAllHistory(newVal) {
  //   const e = document.getElementById('main')
  //   e.scrollTop = e.scrollHeight
  // }
  //}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  border-bottom: 0;
  background: #2b3343 !important;
  //显示侧边导航的图表填充
  svg {
    fill: rgb(216, 228, 238);
  }
  .hamburger-container {
    // background-color:white;
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .chat {
    overflow-y: scroll;
    height: 80vh;
    background: #8cb2d2;
    outline: 10px solid rgb(124, 185, 235);
    box-shadow: 0 0 0 5px rgb(87, 85, 102);
    height: 70vh;
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 5px;
      position: relative;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>


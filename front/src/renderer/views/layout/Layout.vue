<template>
  <div class="app-wrapper" :class="classObj">
    <sidebar class="sidebar-container"></sidebar>
    <div class="main-container">
      <navbar></navbar>
      <app-main></app-main>
    </div>
    <canvas id="canvas" v-show="false"></canvas>
    <video id="video" v-show="false" autoplay></video>
    <div id="console"></div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from "./components";
import ResizeMixin from "./mixin/ResizeHandler";

import { mapGetters } from "vuex";
import ip from "ip";
import MyWebSocket from "../../utils/webSocket";
import MyWebRTC from "../../utils/webRTC";
import CookieUtils from "../../utils/CookieUtil";
import Vue from "vue";
import { getUserMedia } from "../../utils";

import { getNameByIp } from "../../api/user";

export default {
  name: "layout",
  components: {
    Navbar,
    Sidebar,
    AppMain,
  },
  data() {
    return {
      that: null,
      rtc: null,
      toIp: null,
      toIdentity: null,
      remoteStream: null,
      stepInfo: {
        fromIdentity: "",
        toIp: "",
        toIdentity: "",
        type: "stepInfo",
        data: {
          thisStep: 1,
          sumScore: 10,
          preScore: 10,
        },
      },
    };
  },
  created() {
    // if (!this.socket) {
    //   const ip = require('ip')
    //   const localIp = ip.address()
    //   const socket = new MyWebSocket(this.serverIp, this.serverPort, this.localIp, this.roles[0])
    //   Vue.prototype.socket = socket
    //   this.$store.dispatch('set_socket', socket)
    // }
    this.bindSocketMessage();
  },
  mounted() {
    this.canvas = document.getElementById("canvas");
    this.canvas.width = window.innerWidth;
    this.canvas.height = window.innerHeight;
    this.video = document.getElementById("video");
    this.initStudent();
    this.socket.addMessageType("chatToOne", (body) => {
      const data = body.data;
    });
  },
  mixins: [ResizeMixin],
  methods: {
    initStudent() {
      // 用于启动学生webrtc
      console.log("typeof stuRTC " + this.stuRTC);
      if (this.stuRTC === null) {
        CookieUtils.getCookie(ip.address());
        if (
          this.roles[0] === "student" ||
          CookieUtils.cookie.value === "student"
        ) {
          this.initRTC();
          this.startSnapshot(20);
        } else {
          console.log("非学生或者学生初始化失败");
        }
      }
    },
    initRTC() {
      const that = this;
      this.socket.addMessageType("stopMonitor", (body) => {
        that.stuRTC.rtcConnection.close();
        that.stuRTC.rtcConnection = null;

        if (that.remoteStream !== undefined && that.remoteStream !== null) {
          that.remoteStream = null;
        }
        console.log("stopMonitor执行");
        that.rtc = null;
        that.$store.dispatch("rest_stuRTC");
        that.$store.dispatch("rest_remoteStream");
        // that.checkState()
        // that.$notify({
        //   title: '系统通知',
        //   //message: 'ip:' + body.fromIp + '\n身份:' + body.fromIdentity + '\n关闭了实验指导',
        //   message: '\n身份:' + body.fromIdentity + '\n关闭了实验指导',

        //   position: 'bottom-right'
        // })
        this.reload();
      });

      this.socket.addMessageType("offer", (body) => {
        const data = body.data;

        const that = this;
        const rtc = new MyWebRTC(this.socket, this.teachers[0].ip, "teacher");
        rtc.doConnection();
        rtc.rtcConnection.onicecandidate = this.setIce(
          rtc,
          this.teachers[0].ip,
          "teacher"
        );
        getUserMedia((stream) => {
          stream.getTracks().forEach((track) => {
            rtc.rtcConnection.addTrack(track, stream);
          });
        });
        console.log("初始化学生的WEBRTC连接");
        // TODO:ICE和onTrack怎么办？ 暂时放在了student.vue中
        rtc.description =
          "这是与" + this.teachers[0].ip + "teacher连接的webrtc实例";
        this.$store.dispatch("set_stuRTC", rtc);

        rtc.rtcConnection
          .setRemoteDescription(new RTCSessionDescription(data))
          .then(() => {
            console.log("----rtcremoteDesc设置完毕,开始回复answer------");
          });
        rtc.rtcConnection.createAnswer().then((des) => {
          rtc.rtcConnection
            .setLocalDescription(des)
            .then(() => {
              that.socket.sendMessageToOne(
                des,
                "answer",
                rtc.remoteIp,
                rtc.remoteIdentity
              );
              console.log("发送answer完毕");
            })
            .catch((err) => {
              console.log(err);
            });
        });
        that.socket.addMessageType("candidate", (body) => {
          const data = body.data;
          const candidate = new RTCIceCandidate({
            sdpMLineIndex: data.sdpMLineIndex,
            candidate: data.candidate,
          });
          rtc.rtcConnection.addIceCandidate(candidate);
        });
        that.$notify({
          title: "系统通知",
          message: "\n老师正在进行投影讲解，请认真观看教师演示",
          position: "bottom-right",
        });
        that.$router.push("/display/student");
      });
    },
    bindSocketMessage() {
      const that = this;
      // 绑定chatToAll
      this.socket.addMessageType("chatToAll", (body) => {
        const fromIp = body.fromIp;
        const fromIdentity = body.fromIdentity;
        const message = body.data.message;
        const username = body.data.username;
        const date = body.data.date;
        const record = { fromIp, fromIdentity, message, username, date };
        this.$notify({
          title: "【新】课堂消息",
          dangerouslyUseHTMLString: true,
            message:
              "<a href=\"javascript:document.getElementById('openChatBtn').click();\">" +
              message +
              "<a/>",
          position: "bottom-right",
        });
        this.$store.dispatch("push_toAll", record);
      });
      // 绑定
      this.socket.addMessageType("notice", (body) => {
        const fromIp = body.fromIp;
        const fromIdentity = body.fromIdentity;
        const message = body.data.message;
        if (message == "步骤1") {
          document
            .getElementById("expVideo")
            .setAttribute("src", "D:\\videos\\步骤1.mp4");
        } else if (message == "步骤2") {
          document
            .getElementById("expVideo")
            .setAttribute("src", "D:\\videos\\步骤2.mp4");
        } else if (message == "步骤3") {
          document
            .getElementById("expVideo")
            .setAttribute("src", "D:\\videos\\步骤3.mp4");
        } else if (message == "步骤4") {
          document
            .getElementById("expVideo")
            .setAttribute("src", "D:\\videos\\步骤4.mp4");
        } else {
          getNameByIp(fromIp).then((result)=>{
            console.log(result);
          });
         let chatName='王紫剑';

          this.$notify({
            title: "【课堂通知】",
            type: "warning",
            dangerouslyUseHTMLString: true,
            message:
              "<a href=\"javascript:document.getElementById('adminchatEach').click();\">" +
              message +
              "<a/>",
            position: "bottom-right",
          });
        }
      });

      this.socket.addMessageType("monitorOffer", (body) => {
        const data = body.data;
        this.toIp = body.fromIp;
        this.toIdentity = body.fromIdentity;
        /**
         * 将现有webrtc设为空
         * -----------开始---------------
         */
        console.log(typeof this.stuRTC);
        if (typeof this.stuRTC !== "undefined" && this.stuRTC !== null) {
          console.log(this.stuRTC);
          this.stuRTC.rtcConnection.close();
          this.stuRTC.rtcConnection = null;
        }
        // rtc.rtcConnection = null;
        /**
         * -----------结束--------------
         */
        getUserMedia(this.getStream);

        // that.$notify({
        //   title: '【实验指导】',
        //   message:
        //         '用户:' +
        //         body.username +
        //         '\n身份:' +
        //         body.fromIdentity +
        //         '\n正在对你进行指导',
        //   position: 'bottom-right'
        // })
      });
      // 绑定stepInfo,用于获取步骤信息
      this.socket.addMessageType("stepInfo", (body) => {
        const fromIp = body.fromIp;
        const fromidentity = body.fromIdentity;
        const toIp = body.toIp;
        const toidentity = body.toIdentity;
        const data = body.data;
        console.log(
          "step: " +
            data.currentStep +
            "  fromIp: " +
            fromIp +
            " grade_sum:" +
            data.grade_sum
        );
        this.$store.dispatch("push_stepInfo", body);
      });

      // 绑定snapshot
      this.socket.addMessageType("snapShot", (body) => {
        const fromIp = body.fromIp;
        const identity = body.fromIdentity;
        const image = body.data.image;
        const arr = this.$store.state.util.snapshots;
        const newArr = [];
        let exist = false;
        arr.some((obj) => {
          if (obj.ip !== fromIp) {
            newArr.push(obj);
          } else {
            newArr.push({ ip: fromIp, image, identity });
            exist = true;
          }
        });
        if (!exist) {
          newArr.push({ ip: fromIp, image, identity });
        }
        // console.log(newArr)
        this.$store.dispatch("set_snapshots", newArr);
      });
      // 綁定通知
      this.socket.addMessageType("release", (body) => {
        const content = body.data.content;
        const title = body.data.title;
        this.$store.dispatch("push_release", body.data);
        console.log("push_release:" + body.data);
        this.$notify({
          title,
          type: "success",
          message: content,
          position: "bottom-right",
        });
      });
    },
    getStream(stream) {
      // 返回函数，用于获取处理视频流
      const that = this;
      // that.remoteVideo.srcObject = stream
      this.localStream = stream;
      console.log(this.toIp + " ++ " + this.toIdentity);
      const rtc = new MyWebRTC(this.socket, this.toIp, this.toIdentity);
      this.rtc = rtc;
      rtc.doConnection();

      // rtc.rtcConnection.ontrack = this.setAddTrack()
      rtc.rtcConnection.onicecandidate = this.setIce(
        rtc,
        this.toIp,
        this.toIdentity
      );
      this.localStream.getTracks().forEach((track) => {
        rtc.rtcConnection.addTrack(track, this.localStream);
      });
      this.rtc = rtc;
      this.$store.dispatch("set_stuRTC", rtc);
      // this.socket.sendMessage('monitorOffer', 'teacher')
      this.rtc.sendMonitorOffer();
      console.log("开始发送offer");
    },

    setAddTrack(rtc) {
      const that = this;
      return (e) => {
        console.log("监测到对端添加了track，开始取出");
        that.remoteStream = e.streams[0];
        // console.log(that.remoteStream)
        // console.log(e)
        that.remoteVideo.srcObject = this.remoteStream;
        that.startFlag = true;
        that.endFlag = false;
        that.displaying = true;
        that.$store.dispatch("set_remoteStream", e.streams[0]);
      };
    },
    setIce(rtc, ip, identity) {
      const that = this;
      return (e) => {
        if (e.candidate) {
          this.socket.sendMessageToOne(
            {
              type: "candidate",
              sdpMLineIndex: e.candidate.sdpMLineIndex,
              candidate: e.candidate.candidate,
            },
            "candidate",
            ip,
            identity
          );
          // console.log("sendMessagICE")
        } else {
          // this.log = "candidate验证完成"
          console.log("ICE验证完成，rtcDescription:");
        }
      };
    },
    startSnapshot(interval) {
      const that = this;
      console.log(" 身份认证  " + that.roles[0]);
      if (
        that.roles[0] === "undefined"
          ? CookieUtils.getCookie(ip.address()) === "student"
          : true
      ) {
        console.log("如果是学生开始快照,频率:" + interval + "秒");
        getUserMedia((stream) => {
          // 拿到本地视频，首先保存一哈
          console.log("拿到本地视频，保存一下");
          that.$store.dispatch("set_localStream", stream);
          that.video.srcObject = stream;
        });
        setInterval(() => {
          if (that.teachers[0] !== undefined) {
            // 定时采集截图
            that.canvas
              .getContext("2d")
              .drawImage(that.video, 0, 0, canvas.width, canvas.height);
            const src = that.canvas.toDataURL("image/png", 0.7);
            console.log("发送一个图片帧");
            // console.log(src)
            that.socket.sendMessageToOne(
              { image: src },
              "snapShot",
              that.teachers[0].ip,
              "teacher"
            );
          } else {
            console.log("教师未登陆");
          }
        }, interval * 1000);
      } else {
        console.log("不是学生，不进行快照操作");
      }
    },
  },
  watch: {
    $route: "initStudent",
  },
  inject: ["reload"],
  computed: {
    ...mapGetters(["roles", "teachers", "snapshots", "stepInfos", "stuRTC"]),
    sidebar() {
      return this.$store.state.app.sidebar;
    },
    device() {
      return this.$store.state.app.device;
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === "mobile",
      };
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "../../styles/mixin.scss";
.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
}
</style>

import store from '@/store'
export default class MyWebRTC {
    /** rtc连接实例 */
    rtcConnection = null
    /** 连接描述 */
    description = '暂无'
    /** socket对象 */
    socket = null
    /** remote Ip */
    remoteIp = null
    /** remote Identity */
    remoteIdentity = null

    /*
     *@functionName: 构造函数
     *@params1: webRtc构造函数
     *@params2: 对端ip
     *@params3: 对端身份
     *@description:
     *@author: LiuFan
     *@date: 2019-09-26 13:00:49
    */
    constructor(socket, toIp, toIdentity) {
      this.socket = socket
      this.onMessageOffer()
      this.remoteIp = toIp
      this.remoteIdentity = toIdentity
      console.log(this)
    }

    onMessageOffer() {
      const that = this
      this.socket.addMessageType('answer', (body) => {
        const data = body.data
        that.rtcConnection.setRemoteDescription(new RTCSessionDescription(data)).then(() => {
          console.log('----rtcremoteDesc设置完毕------')
          console.log(that.rtcConnection)
        })
      })
      this.socket.addMessageType('offer', (body) => {
        const data = body.data
        that.rtcConnection.setRemoteDescription(new RTCSessionDescription(data)).then(() => {
          console.log('----rtcremoteDesc设置完毕,开始回复answer------')
        })
        that.rtcConnection.createAnswer().then(des => {
          that.rtcConnection.setLocalDescription(des).then(() => {
            that.socket.sendMessageToOne(des, 'answer', that.remoteIp, that.remoteIdentity)
            console.log('发送answer完毕')
          }).catch(err => {
            console.log(err)
          })
        })
      })
      this.socket.addMessageType('candidate', (body) => {
        const data = body.data
        const candidate = new RTCIceCandidate({
          sdpMLineIndex: data.sdpMLineIndex,
          candidate: data.candidate
        })
        that.rtcConnection.addIceCandidate(candidate)
      })
    }

    /*
     *@functionName:建立webRTC连接过程
     *@description:
     1、第一步：new RTCPeerConnection对象，serveInfo作为参数传递
     2、第二部：为RTCPeerConnection实例指定几个方法
        2.1、onicecandidate 做candidate验证的回调函数
        2.2、ontrack、onremovetrack 做对端添加删除媒体的回调函数
     3、第三步：进行信令交换，该端首先创建offer发送至服务器，需要指明建立对象
     *@author: LiuFan
     *@date: 2019-09-26 10:59:38
    */
    doConnection() {
      // 实例化连接对象
      const rtc = new RTCPeerConnection(serveInfo)
      const that = this
      // 绑定验证回调函数
      // rtc.onicecandidate = this.onIceCandidate
      // 绑定对端添加轨道得回调
      rtc.ontrack = this.onRemoteAddTrack
      // 绑定对端删除轨道的回调
      rtc.onremovetrack = this.onRemoteRemoveTrack
      this.rtcConnection = rtc
    }

    /**
     * 发送offer
     */
    sendOffer() {
      const rtc = this.rtcConnection
      const that = this
      rtc.createOffer().then(des => {
        rtc.setLocalDescription(des).then(() => {
          that.socket.sendMessageToOne(des, 'offer', that.remoteIp, that.remoteIdentity)
          console.log('发送offer')
        }).catch(err => {
          console.log(err)
        })
      })
    }
    sendMonitorOffer() {
      const rtc = this.rtcConnection
      const that = this
      rtc.createOffer().then(des => {
        rtc.setLocalDescription(des).then(() => {
          that.socket.sendMessageToOne(des, 'monitorOffer', that.remoteIp, that.remoteIdentity)
          console.log('发送monitorOffer')
        }).catch(err => {
          console.log(err)
        })
      })
    }

    // onIceCandidate(e){
    //     console.log("----onIceCandidate------")
    //     if (e.candidate) {
    //         this.socket.sendMessageToOne({
    //                 type: 'candidate',
    //                 sdpMLineIndex: e.candidate.sdpMLineIndex,
    //                 candidate: e.candidate.candidate
    //         },'candidate',this.remoteIp,this.remoteIdentity)
    //     }else {
    //         // this.log = "candidate验证完成"
    //         console.log("ICE验证完成，rtcDescription:"+this.description)
    //     }
    //
    //
    //
    // }
    onRemoteAddTrack(e) {
      console.log('对端添加了一个track')
      console.log(e)
      store.dispatch('set_remoteStream', e.streams[0])
    }

    onRemoteRemoveTrack(e) {
      console.log('对端移除了一个track')
      console.log(e)
      store.dispatch('reset_remoteStream')
    }

    configOnTrack(callBack) {
      if (this.rtcConnection) {
        this.rtcConnection.ontrack = callBack
      } else {
        console.log('rtcConnection:' + this.rtcConnection)
      }
    }

    configOnRemoveTrack(callBack) {
      if (this.rtcConnection) {
        this.rtcConnection.onremovetrack = callBack
      } else {
        console.log('rtcConnection:' + this.rtcConnection)
      }
    }
}

/** ServeInfo */
const
  serveInfo = {
    'iceServers': [
      {
        'urls': 'turn:numb.viagenie.ca',
        'username': 'muazkh',
        'credential': 'webrtc@live.com'
      },

      {
        'urls': 'stun:stun.l.google.com:19302'
      }
    ], mandatory: {
      OfferToReceiveVideo: true
    }
  }

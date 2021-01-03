import Vue from 'vue'
import { Message } from 'element-ui'
/**
 *
 * @param rtc rtc实例
 * @param ip 对端ip
 * @param identity 对端身份
 * @returns {Function}
 */
export function setRtcIce(rtc, ip, identity) {
  return (e) => {
    console.log('----onIceCandidate------')
    if (e.candidate) {
      Vue.prototype.socket.sendMessageToOne({
        type: 'candidate',
        sdpMLineIndex: e.candidate.sdpMLineIndex,
        candidate: e.candidate.candidate
      }, 'candidate', ip, identity)
    } else {
      console.log('ICE验证完成，rtcDescription:' + rtc.description)
    }
  }
}

/**
 *
 * @param rtc rtc实例
 * @param remoteStream 用于接收对端视频流的变量
 * @param element 用于播放视频流的Dom
 * @returns {Function}
 */
export function setAddTrack(rtc, remoteStream, element) {
  return (e) => {
    console.log('接收来自对端的视频流')
    console.log(e)
    remoteStream = e.streams[0]
    // if(element)
    //   Message.error("提供的DOM容器无效")
    // else
    element.srcObject = e.streams[0]
  }
}

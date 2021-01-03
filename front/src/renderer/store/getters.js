const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  getRtc: state => (remoteIp, remoteIdentity) => {
    return state.util.rtcPool.filter(rtc => {
      return rtc.remoteIp === remoteIp && rtc.remoteIdentity === remoteIdentity
    })
  },
  teachers: state => state.util.teachers,
  students: state => state.util.students,
  stepInfos: state => (ip) => {
    return state.util.stepInfos[ip]
  },
  stepInfosList: state => state.util.stepInfos,
  RTCs: state => state.util.rtcPool,
  stuRTC: state => state.util.studentRtc,
  remote_stream: state => state.util.remoteStream,
  toAllHistory: state => state.chat.toAllHistory,
  toOneHistory: state => state.chat.toOneHistory,
  teacher_remoteStream: state => state.util.teacher_remoteStream,
  vsocket: state => state.util.socket,
  expInfo: state => state.chat.expInfo,
  stepError: state => state.chat.stepError,
  localStream: state => state.util.localStream,
  getSnapShots: state => (ip) => {
    return state.util.snapshots.filter(obj => {
      return obj.ip === ip
    })
  },
  snapshots: state => state.util.snapshots,
  release: state => state.chat.release
}
export default getters

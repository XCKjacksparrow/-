import Vue from 'vue'

export const util = {
  state: {
    socket: null,
    rtcPool: [],
    teachers: [],
    students: [],
    stepInfos: {},
    studentRtc: null,
    remoteStream: null,
    teacher_remoteStream: null,
    localStream: null,
    snapshots: []
  },
  mutations: {
    PUSH_RTCPOOL: (state, rtc) => {
      state.rtcPool.push(rtc)
      console.log(state.rtcPool)
    },
    RESET_RTCPOOL: (state) => {
      state.rtcPool = []
    },
    SET_PEOPLE: (state, data) => {
      state.teachers = data.teachers
      state.students = data.students
    },
    PUSH_STEPINFO: (state, data) => {
      // 区分ip
      if (state.stepInfos[data.fromIp] !== undefined) {
        const arr = state.stepInfos[data.fromIp]
        arr.push(data.data)
        Vue.set(state.stepInfos, data.fromIp, arr)
      } else {
        // state.stepInfos[data.fromIp] = [data.data]
        Vue.set(state.stepInfos, data.fromIp, [data.data])
      }
      console.log('更新stepInfo')
    },
    REST_STEPINFO: (state) => {
      state.stepInfos = []
    },
    SET_STEPINFO: (state, data) => {
      Vue.set(state.stepInfos, data.fromIp, data.data)
    },
    SET_STURTC: (state, rtc) => {
      state.studentRtc = rtc
    },
    REST_STURTC: (state) => {
      state.studentRtc = null
    },
    SET_REMOTE_STREAM: (state, s) => {
      state.remoteStream = s
    },
    REST_REMOTE_STREAM: (state) => {
      state.remoteStream = null
    },
    SET_TSTREAM: (state, s) => {
      state.teacher_remoteStream = s
    },
    SET_SOCKET: (state, s) => {
      state.socket = s
    },
    SET_LOCAL_STREAM: (state, s) => {
      state.localStream = s
    },
    SET_SNAPSHOTS: (state, arr) => {
      state.snapshots = arr
    }
  },
  actions: {
    push_rtcpool: ({ commit }, rtc) => {
      commit('PUSH_RTCPOOL', rtc)
    },
    rest_rtcpool: ({ commit }, rtc) => {
      commit('RESET_RTCPOOL', rtc)
    },
    set_people: ({ commit }, data) => {
      commit('SET_PEOPLE', data)
    },
    push_stepInfo: ({ commit }, data) => {
      commit('PUSH_STEPINFO', data)
    },
    set_stepInfo: ({ commit }, data) => {
      commit('SET_STEPINFO', data)
    },
    rest_stepInfo: ({ commit }) => {
      commit('REST_STEPINFO')
    },
    set_stuRTC: ({ commit }, rtc) => {
      commit('SET_STURTC', rtc)
      return new Promise(resolve => {
        resolve()
      }, reject => {
        reject()
      })
    },
    rest_stuRTC: ({ commit }) => {
      commit('REST_STURTC')
    },
    set_remoteStream: ({ commit }, s) => {
      commit('SET_REMOTE_STREAM', s)
    },
    rest_remoteStream: ({ commit }) => {
      commit('REST_REMOTE_STREAM')
    },
    set_teacher_remoteStream: ({ commit }, s) => {
      commit('SET_TSTREAM', s)
    },
    set_socket: ({ commit }, socket) => {
      commit('SET_SOCKET', socket)
    },
    set_snapshots: ({ commit }, arr) => {
      commit('SET_SNAPSHOTS', arr)
    },
    set_localStream: ({ commit }, s) => {
      commit('SET_LOCAL_STREAM', s)
    }
  }
}

export const chat = {
  state: {
    toAllHistory: [],
    toOneHistory: [],
    expInfo: {
      steps: [{
        stepId: '1',
        stepContain: '正确放置坩埚、纸漏斗',
        score: '10',
        isDanger: true, // 决定dangermessage的显示
        dangerMessage: ['请小心操作', '请加快实验进度'] // 有用
      }, {
        stepId: '2',
        stepContain: '取出铝粉药剂瓶、氧化铁药剂瓶，放置滤纸和药匙',
        score: '10',
        isDanger: true,
        dangerMessage: ['请小心操作', '请加快实验进度']
      }, {
        stepId: '3',
        stepContain: '打开铝粉药剂瓶，打开氧化铁药剂瓶',
        score: '10',
        isDanger: true,
        dangerMessage: ['请小心操作', '请加快实验进度']
      }, {
        stepId: '4',
        stepContain: '用药匙取铝粉，用药匙取氧化铁粉末，氧化铁粉末和铝粉比例为3:2',
        score: '20',
        isDanger: true,
        dangerMessage: ['请小心操作', '请加快实验进度']
      }, {
        stepId: '5',
        stepContain: '将铝粉和氧化铁粉末转移至纸漏斗中',
        score: '10',
        isDanger: true,
        dangerMessage: ['请小心操作', '请加快实验进度']
      }, {
        stepId: '6',
        stepContain: '用坩埚钳夹住镁条插在混合物中',
        score: '20',
        isDanger: true,
        dangerMessage: ['请小心操作', '请加快实验进度']
      }, {
        stepId: '7',
        stepContain: '取出酒精灯并点燃，用酒精灯点燃镁条',
        score: '20',
        isDanger: true,
        dangerMessage: ['镁条会剧烈燃烧，点燃后请尽快远离', '请小心使用酒精灯', '酒精灯必须用灯帽盖灭，不可用嘴去吹。']
      }],
      expTitle: '铝热反应'
    },
    stepError: {},
    release: []
  },
  mutations: {

    PUSH_TO_ALL: (state, record) => {
      state.toAllHistory.push(record)
    },

    RESET_TO_ALL: (state) => {
      state.toAllHistory = []
    },
    PUSH_TO_ONE: (state, record) => {
      state.toOneHistory.push(record)
    },
    RESET_TO_ONE: (state) => {
      state.toOneHistory = []
    },
    SET_EXP_INFO: (state, expInfo) => {
      state.expInfo = expInfo
    },
    SET_STEP_ERROR: (state, stepError) => {
      state.stepError = stepError
    },
    PUSH_RELEASE: (state, release) => {
      state.release.push(release)
    },
    RESET_RELEASE: (state) => {
      state.release = []
    }
  },
  actions: {

    push_toAll: ({ commit }, record) => {
      commit('PUSH_TO_ALL', record)
    },
    push_toOne: ({ commit }, record) => {
      commit('PUSH_TO_ONE', record)
    },
    reset_toOne: ({ commit }) => {
      commit('RESET_TO_ONE')
    },
    reset_toAll: ({ commit }) => {
      commit('RESET_TO_ALL')
    },
    set_expInfo({ commit }, expInfo) {
      commit('SET_EXP_INFO', expInfo)
    },
    set_stepError({ commit }, stepError) {
      commit('SET_STEP_ERROR', stepError)
    },
    push_release({ commit }, release) {
      commit('PUSH_RELEASE', release)
    },
    reset_release({ commit }) {
      commit('RESET_RELEASE')
    }

  }
}

import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import getters from './getters'
import { util } from './modules/util'
import { chat } from './modules/chat'
import createPersistedState from 'vuex-persistedstate'
Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    util,
    chat
  },
  getters
  // plugins:[createPersistedState()]
})

export default store

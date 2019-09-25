import Vue from 'vue'
import Vuex from 'vuex'

import init from './modules/init'
import login from './modules/login'

//use vuex
Vue.use(Vuex);


// Export module
export default new Vuex.Store({
  modules:{
    init,
    login
  }
})

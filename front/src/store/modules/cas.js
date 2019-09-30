import HttpUtils from '../../utils/httpUtils/axiosUtils'
/**
 * 变量参数
 */
const state = {
}

/**
 * Getters
 * @type {Object}
 */
const getters = {}

/**
 * Mutations
 *
 * @type {Object}
 */
const mutations = {
  getTicket(state, info) {
    HttpUtils.post('Cas/GetTicket', info).then(
      function (resp) {
        console.log("getTicket",resp)
        if(resp){
          window.open(info.service+"?ticket="+resp,"_self")
        }
      }
    );
  }
}

/**
 * Actions
 * @type {Object}
 */
const actions = {
}

// Export module
export default {
  state,
  getters,
  mutations,
  actions
}

/**
 * Main app store
 *
 * @author vmalyshev
 * @date 21.03.2022
 */

import Vue from 'vue';
import Vuex from 'vuex'
import getters from './getters'
import mutations from './mutations'
import state from './state'
import actions from './actions';

Vue.use(Vuex)

const store = new Vuex.Store({
	state,
	mutations,
	getters,
	actions
})

export default store;

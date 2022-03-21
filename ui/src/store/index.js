/**
 * Main app store
 *
 * @author vmalyshev
 * @date 21.03.2022
 */

import Vuex from 'vuex'
import getters from './getters'
import mutations from './mutations'
import state from './state'
import actions from './actions';

let store;

const getStore = () => {
	if (!store) {
		store = new Vuex.Store({
			state,
			mutations,
			getters,
			actions
		})
	}
	return store;
}

export default getStore;

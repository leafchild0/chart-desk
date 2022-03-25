import api from '@/api';

/**
 * Actions of the app
 *
 * @author vmalyshev
 * @date 21.03.2022
 */

const setToken = ({ commit }, token) => {
	if (window.sessionStorage) {
		sessionStorage.setItem('token', token);
	}
	commit('setToken', token);
}

const logout = ({ dispatch }) => {
	dispatch('setToken', '')
	window.location.assign('/');
}

const checkAndPopulateToken = ({ commit }) => {
	if (window.sessionStorage) {
		const userToken = sessionStorage.getItem('token');
		if (userToken) {
			commit('setToken', userToken);
		}
	}
}

const updateUser = ({ commit }, user) => {
	// Update user in store
	commit('updateUser', user);
}

export default {
	setToken,
	checkAndPopulateToken,
	updateUser,
	logout
}

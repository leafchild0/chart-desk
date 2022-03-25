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

const logout = ({ commit }) => {
	commit('setToken', '')
	window.location.assign('/');
}

const login = ({ dispatch }, payload, notif) => {
	api.login(payload).then(response => {
		dispatch('setToken', response.data.accessToken);
		window.location.replace('/');
	}).catch(err => {
		if (err.response?.status === 401) {
			notif.e('Username or Password is incorrect');
		} else {
			notif.e('Ups... Something went wrong');
		}
	})
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
	logout,
	login
}

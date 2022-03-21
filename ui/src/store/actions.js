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

export default {
	setToken
}

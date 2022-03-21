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

const checkAndPopulateToken = ({ commit }) => {
	if (window.sessionStorage) {
		const userToken = sessionStorage.getItem('token');
		if (userToken) {
			commit('setToken', userToken);
		}
	}
}

export default {
	setToken,
	checkAndPopulateToken
}

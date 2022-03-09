/**
 * Token util
 * @author victor
 * @date 09.03.2022
 */

let token = '';

const checkAndPopulateToken = () => {

	if (window.sessionStorage) {
		const userToken = sessionStorage.getItem('token');
		if (userToken) {
			token = userToken;
		}
	}
}

const setToken = (newToken) => {

	if (window.sessionStorage && newToken) {
		token = newToken;
		sessionStorage.setItem('token', token);
	}
}

const getToken = () => {
	return token;
}

export default {
	checkAndPopulateToken,
	getToken,
	setToken
};

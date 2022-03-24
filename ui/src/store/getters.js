/**
 * Getter for the state
 *
 * @author vmalyshev
 * @date 21.03.2022
 */

const getCurrentUser = (state) => {
	return state.user;
}

const getUserToken = (state) => {
	return state.token;
}

export default {
	getCurrentUser,
	getUserToken
}

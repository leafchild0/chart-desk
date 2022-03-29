/**
 * Getter for the state
 *
 * @author vmalyshev
 * @date 21.03.2022
 */

const currentUser = (state) => {
	return state.user;
}

const userToken = (state) => {
	return state.token;
}

export default {
	currentUser,
	userToken
}

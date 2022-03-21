/**
 * All app mutations
 *
 * @author vmalyshev
 * @date 21.03.2022
 */

const updateUser = (state, user) => {
	state.user = user;
}

const setToken = (state, token) => {
	state.token = token;
}

export default {
	updateUser,
	setToken
}

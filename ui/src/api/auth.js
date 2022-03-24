import api from './api';

/**
 * API related to Auth and user, i.e. login/signup
 *
 * @author vmalyshev
 * @date 24.03.2022
 */

export const login = (payload) => {
	return api.post('api/auth/login', payload)
}

export const signUp = (payload) => {
	return api.post('api/auth/signup', payload)
}

export const getCurrentUser = () => {
	return api.get('api/auth/user')
}


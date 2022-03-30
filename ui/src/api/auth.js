import api from './api';

/**
 * API related to Auth and user, i.e. login/signup
 *
 * @author vmalyshev
 * @date 24.03.2022
 */

export const login = (payload) => {
	return api.post('auth/login', payload)
}

export const signUp = (payload) => {
	return api.post('auth/signup', payload)
}

export const getCurrentUser = () => {
	return api.get('auth/user')
}

export const getAllUsers = () => {
	return api.get('auth/users')
}

export const updateUserDetails = (payload) => {
	return api.put('auth/user', payload)
}

export const updatePassword = (payload) => {
	return api.post('auth/password', payload)
}


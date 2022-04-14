import {auth} from './api';

/**
 * auth related to auth and user, i.e. login/signup
 *
 * @author vmalyshev
 * @date 24.03.2022
 */

export const login = (payload) => {
	return auth.post('login', payload)
}

export const signUp = (payload) => {
	return auth.post('signup', payload)
}

export const getCurrentUser = () => {
	return auth.get('user')
}

export const getAllUsers = () => {
	return auth.get('users')
}

export const updateUserDetails = (payload) => {
	return auth.put('user', payload)
}

export const deactivateUser = (id) => {
	return auth.post(`user/${id}/deactivate`)
}

export const updatePassword = (payload) => {
	return auth.post('password', payload)
}


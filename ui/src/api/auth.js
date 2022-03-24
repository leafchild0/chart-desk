import api from './api';

/**
 *
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

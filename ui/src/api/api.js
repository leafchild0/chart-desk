/**
 * All API for all services
 *
 * @author vmalyshev
 * @date 24.03.2022
 */

import store from '@/store'
import axios from 'axios'

export const auth = axios.create({
	baseURL: process.env.API_URL + '/auth/'
})

auth.interceptors.request.use(config => {

	config.headers.common['Authorization'] = 'Bearer ' + store.getters['token']
	return config
})

export const charts = axios.create({
	baseURL: process.env.API_URL + '/charts/'
});

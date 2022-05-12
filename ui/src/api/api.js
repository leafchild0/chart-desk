/**
 * All API for all services
 *
 * @author vmalyshev
 * @date 24.03.2022
 */

import store from '@/store'
import axios from 'axios'

export const api = axios.create({
	baseURL: process.env.VUE_APP_API_URL
})

api.interceptors.request.use(config => {

	config.headers.common['Authorization'] = 'Bearer ' + store.getters['token']
	return config
})

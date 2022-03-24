/**
 *
 *
 * @author vmalyshev
 * @date 24.03.2022
 */

import store from '@/store'
import axios from 'axios';

const api = axios.create({})

api.interceptors.request.use(config => {

	config.headers.common['Authorization'] = 'Bearer ' + store.getters['token']
	return config
})

export default api;

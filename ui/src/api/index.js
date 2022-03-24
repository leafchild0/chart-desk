/**
 * Special axios instance with token passed
 *
 * @author victor
 * @date 09.03.2022
 */

import axios from 'axios'
import store from '@/store'

const authInstance = axios.create({})

authInstance.interceptors.request.use(config => {
	
	config.headers.common['Authorization'] = 'Bearer ' + store.getters['token']
	return config
})

export default authInstance;

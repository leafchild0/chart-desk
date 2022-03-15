/**
 * Special axios instance with token passed
 *
 * @author victor
 * @date 09.03.2022
 */

import axios from 'axios'
import tokenManager from './tokenManager'

const authInstance = axios.create({})

authInstance.interceptors.request.use(config => {

	config.headers.common['Authorization'] = 'Bearer ' + tokenManager.getToken()
	return config
})

export default authInstance;

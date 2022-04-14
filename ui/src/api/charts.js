import {charts} from './api'
import store from '@/store'

export const chartsList = () => {
	const username = store.getters['user']?.username;
	// Get all charts by username
	return charts.get(username)
}

export const uploadChart = (payload) => {
	const username = store.getters['user']?.username;
	// Upload by username and body
	return charts.post(username, payload);
}

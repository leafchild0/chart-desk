import api from './api';

export const chartsList = (userId) => {
	return api.get('charts/' + userId)
}

export const getChart = (userId, id) => {
	return api.get(`charts/${userId}/${id}`)
}

export const uploadChart = (userId, payload) => {
	return api.post('charts/' + userId, payload);
}

export const pullCharts = (payload) => {
	return api.post('/proxy/index', payload);
}

export const uploadCharts = (userId, payload) => {
	return api.post('/proxy/' + userId, payload);
}

import api from './api';

export const chartsList = (username) => {
	return api.get('charts/' + username)
}

export const getChart = (username, id) => {
	return api.get(`charts/${username}/${id}`)
}

export const uploadChart = (username, payload) => {
	return api.post('charts/' + username, payload);
}

export const pullCharts = (payload) => {
	return api.post('/proxy/index', payload);
}

export const uploadCharts = (username, payload) => {
	return api.post('/proxy/' + username, payload);
}

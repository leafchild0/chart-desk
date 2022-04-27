import api from './api';

export const chartsList = () => {
	return api.get('charts/2')
}

export const getChart = (id) => {
	return api.get(`charts/2/${id}`)
}

export const uploadChart = (payload) => {
	return api.post('charts/2', payload);
}

import axios from 'axios'

const chartsApi = axios.create({
	baseURL: '/charts_api/api'
});

export const chartsList = () => {
	return chartsApi.get('/2/charts')
}

export const getChart = (name, version) => {
	return chartsApi.get(`2/${name} ${version ? '?version=' + version : ''}`)
}

export const uploadChart = (payload) => {
	return chartsApi.post('/2/charts', payload);
}

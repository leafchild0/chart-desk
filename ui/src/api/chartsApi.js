import axios from 'axios'

const chartsApi = axios.create({
	baseURL: '/charts_api/api'
});

export const chartsList = () => {
	return chartsApi.get('/2/charts')
}

export const uploadChart = (payload) => {
	return chartsApi.post('/2/charts', payload);
}

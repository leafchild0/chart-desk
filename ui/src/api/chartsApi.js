import axios from 'axios'

const chartsApi = axios.create({
	baseURL: '/charts_api/'
});

export const chartsList = () => {
	// TODO: user id/name here & gateway
	return chartsApi.get('2/index.json', {
		dataType: 'json',
	})
}

export const uploadChart = (payload) => {
	return chartsApi.post('api/2/charts', payload, {});
}

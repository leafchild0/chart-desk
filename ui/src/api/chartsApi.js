import axios from 'axios'

const chartsApi = axios.create({
	baseURL: '/charts_api/'
});

export default chartsApi;

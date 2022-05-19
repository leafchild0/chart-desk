import {api} from './api';

export const chartsList = (username) => {
	return api.get('/charts/' + username)
}

export const getChart = (username, id) => {
	return api.get(`/charts/${username}/${id}`)
}

export const uploadChart = (username, payload) => {
	return api.post('/charts/' + username, payload);
}

export const pullCharts = (payload) => {
	return api.post('/charts/proxy/index', payload);
}

export const uploadCharts = (username, payload) => {
	return api.post('/charts/proxy/' + username, payload);
}

export const tagList = () => {
	return api.get('/charts/tags');
}

export const createTag = (tag) => {
	return api.post('/charts/tags', {name: tag});
}

export const assignTag = (tagId, payload) => {
	return api.post('/charts/tags/' + tagId + '/assign', payload);
}

export const unassignTag = (tagId, payload) => {
	return api.post('/charts/tags/' + tagId + '/unassign', payload);
}

export const getAllChartVersions = (username, name) => {
	return api.get(`charts/${username}/${name}/versions`);
}

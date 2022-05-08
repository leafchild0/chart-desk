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

export const tagList = () => {
	return api.get('tags');
}

export const createTag = (tag) => {
	return api.post('tags', {name: tag});
}

export const assignTag = (tagId, payload) => {
	return api.post('tags/' + tagId + '/assign', payload);
}

export const unassignTag = (tagId, payload) => {
	return api.post('tags/' + tagId + '/unassign', payload);
}

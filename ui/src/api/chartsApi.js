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

export const tagList = () => {
	return api.get('tags');
}

export const createTag = (tag) => {
	return api.post('tags', {name: tag});
}

export const assignTag = (tagId, payload) => {
	return api.post('tags/' + tagId + '/assign', payload);
}

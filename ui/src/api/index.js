/**
 * Special axios instance with token passed
 *
 * @author victor
 * @date 09.03.2022
 */

import {deactivateUser, getAllUsers, getCurrentUser, login, signUp, updatePassword, updateUserDetails} from './auth';
import {chartsList, pullCharts, uploadCharts, getChart, uploadChart, createTag, tagList, unassignTag, assignTag} from './chartsApi';

export default {
	login,
	signUp,
	getCurrentUser,
	updatePassword,
	updateUserDetails,
	getAllUsers,
	deactivateUser,
	chartsList,
	uploadChart,
	getChart,
	pullCharts,
	uploadCharts,
	tagList,
	createTag,
	assignTag,
	unassignTag
};

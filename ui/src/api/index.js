/**
 * Special axios instance with token passed
 *
 * @author victor
 * @date 09.03.2022
 */

import {deactivateUser, getAllUsers, getCurrentUser, login, signUp, updatePassword, updateUserDetails} from './auth';
import {assignTag, chartsList, createTag, getAllChartVersions, getChart, pullCharts, tagList, unassignTag, uploadChart, uploadCharts} from './chartsApi';

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
	unassignTag,
	getAllChartVersions
};

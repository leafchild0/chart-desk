/**
 * Special axios instance with token passed
 *
 * @author victor
 * @date 09.03.2022
 */

import {deactivateUser, getAllUsers, getCurrentUser, login, signUp, updatePassword, updateUserDetails} from './auth';
import {chartsList, uploadChart} from './charts';

export default {
	login,
	signUp,
	getCurrentUser,
	updatePassword,
	updateUserDetails,
	getAllUsers,
	deactivateUser,
	chartsList,
	uploadChart
};

/**
 * Special axios instance with token passed
 *
 * @author victor
 * @date 09.03.2022
 */

import {
	login,
	signUp,
	getCurrentUser,
	updatePassword,
	updateUserDetails,
	getAllUsers,
	deactivateUser
} from './auth';

export default {
	login,
	signUp,
	getCurrentUser,
	updatePassword,
	updateUserDetails,
	getAllUsers,
	deactivateUser
};

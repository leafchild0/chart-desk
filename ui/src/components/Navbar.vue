<template>
	<section>
		<b-navbar type='is-light'>
			<template #brand>
				<b-navbar-item tag='router-link' :to='{ path: "/" }'>
					<b-icon icon='home'></b-icon>
				</b-navbar-item>
			</template>
			<template #start>
				<b-navbar-item href='/charts'>
					Charts
				</b-navbar-item>
				<b-navbar-dropdown label='Info'>
					<b-navbar-item href='/about'>
						About
					</b-navbar-item>
				</b-navbar-dropdown>
			</template>
			<template #end>
				<b-navbar-item tag='div'>
					<b-navbar-item @click="showUserAccount()">
						<b-icon icon='account'></b-icon>
					</b-navbar-item>
					<b-navbar-item @click="logout()">
						<b-icon icon='location-exit'></b-icon>
					</b-navbar-item>
				</b-navbar-item>
			</template>
		</b-navbar>
		<b-modal
			:active="isUserAccountActive"
			has-modal-card
			trap-focus
			:destroy-on-hide="false"
			:can-cancel="['outside']"
			aria-modal>
			<template>
				<UserAccount v-bind="currentUser" @close="closeUserAccount" @save="saveUserInfo" @change-password="changePassword">
				</UserAccount>
			</template>
		</b-modal>
	</section>
</template>

<script>
	import UserAccount from '@/components/UserAccount';
	import {mapActions, mapGetters} from 'vuex';
	import api from '@/api';

	export default {
		name: 'Navbar',
		components: {UserAccount},
		data() {
			return {
				isUserAccountActive: false
			}
		},
		computed: {
			...mapGetters([
				'currentUser',
			])
		},
		methods: {
			...mapActions([
				'updateUser',
			]),
			logout() {
				// Just remove the token
				this.$store.dispatch('logout')
			},
			showUserAccount() {
				this.isUserAccountActive = true;
			},
			closeUserAccount() {
				this.isUserAccountActive = false;
			},
			saveUserInfo(userInfo) {
				api.updateUserDetails(userInfo)
					.then(response => {
						this.updateUser(response);
						this.$toastr.s('User Info has been updated');
						this.isUserAccountActive = false;
					})
					.catch(() => {
						this.$toastr.e('Error during user details update');
					})
			},
			changePassword(passwordInfo) {
				api.updatePassword(passwordInfo)
					.then(response => {
						if (response.status === 200) {
							this.$toastr.s('Password has been changed successfully');
							this.isUserAccountActive = false;
						}
					})
					.catch(() => {
						this.$toastr.e('Error during user details update');
					})
			}
		}
	}
</script>

<style scoped>

</style>

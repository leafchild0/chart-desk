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
                <UserAccount v-bind="userDetails" @close="closeUserAccount" @save="saveUserInfo" @changePassword="changePassword"></UserAccount>
            </template>
        </b-modal>
    </section>
</template>

<script>
	import tokenManager from '@/auth/tokenManager';
	import UserAccount from '@/components/UserAccount';

	export default {
		name: 'Navbar',
		components: {UserAccount},
		data() {
			return {
				isUserAccountActive: false,
				userDetails: {}
			}
		},
		methods: {
			logout() {
				// Just remove the token
				tokenManager.setToken('');
				window.location.assign('/');
			},
			showUserAccount() {
				this.isUserAccountActive = true;
			},
			closeUserAccount() {
				this.isUserAccountActive = false;
			},
			saveUserInfo(userInfo) {
				console.log(userInfo);
			},
			changePassword(newPassword) {
				console.log(newPassword);
			}
		}
	}
</script>

<style scoped>

</style>

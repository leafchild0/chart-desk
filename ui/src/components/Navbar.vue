<template>
    <b-navbar type='is-light'>
        <template #brand>
            <b-navbar-item tag='router-link' :to='{ path: "/" }'>
                <b-icon icon='home'></b-icon>
            </b-navbar-item>
        </template>
        <template #start>
            <b-navbar-item href='/'>
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
</template>

<script>
	import tokenManager from '@/auth/tokenManager';

	export default {
		name: 'Navbar',
		methods: {
			logout: () => {
				// Just remove the token
				tokenManager.setToken('');
				window.location.assign('/');
			},
			showUserAccount: () => {
				console.log('show User Account');
			}
		}
	}
</script>

<style scoped>

</style>

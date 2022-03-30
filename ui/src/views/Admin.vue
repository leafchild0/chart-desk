<template>
	<div class='admin'>
		<Navbar/>
		<div class='is-admin' v-if='!currentUser.isAdmin'>You don't have permission to access this page</div>
		<div v-else>
			<UserTable :data='users' @deactivate='deactivateUser' :filter-columns='filterColumns' :headers='headers'/>
		</div>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';
	import {mapGetters} from 'vuex';
	import UserTable from '@/components/UserTable';

	export default {
		name: 'Admin',
		components: {Navbar, UserTable},
		data() {
			return {
				users: [],
				headers: [
					{field: 'username', label: 'User name'},
					{field: 'firstName', label: 'First Name'},
					{field: 'lastName', label: 'Last Name'},
					{field: 'email', label: 'Email'},
					{field: 'isAdmin', label: 'Admin'}
				]
			}
		},
		computed: {
			...mapGetters([
				'currentUser',
			]),
			filterColumns() {
				return this.headers.filter(h => h.field !== 'admin').map(h => h.field)
			}
		},
		methods: {
			deactivateUser(id) {
				api.deactivateUser(id)
					.then(() => {
						this.$toastr.s('User Info has been deactivated');
						// Remove user for consistency
						this.users = this.users.filter(u => u.id !== id);
					})
					.catch(() => {
						this.$toastr.e('Error during user details update');
					})
			}
		},
		mounted() {
			// There should be a better place for this
			api.getAllUsers()
				.then(response => {
					this.users = response.data.filter(u => u.id !== this.currentUser.id);
				})
				.catch(() => {
					this.$toastr.e('Ups... Something went wrong during users fetch');
				})
		}
	}
</script>

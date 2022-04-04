<template>
	<div class='admin'>
		<Navbar/>
		<div class='is-admin' v-if='!currentUser.isAdmin'>You don't have permission to access this page</div>
		<div v-else>
			<FilterableTable :data='users' :filter-columns='filterColumns' :headers='headers'>
				<template v-slot:actions='props'>
					<b-tooltip label='Deactivate user' position='is-left' type='is-warning'>
						<b-button size='is-small' type='is-danger' outlined rounded icon-left='account-off' @click='() => deactivateUser(props.id)'></b-button>
					</b-tooltip>
				</template>
			</FilterableTable>
		</div>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';
	import {mapGetters} from 'vuex';
	import FilterableTable from '@/components/FilterableTable';

	export default {
		name: 'Admin',
		components: {Navbar, FilterableTable},
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
				return this.headers.filter(h => h.field !== 'isAdmin').map(h => h.field)
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

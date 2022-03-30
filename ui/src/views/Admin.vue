<template>
	<div class='admin'>
		<Navbar/>
		<div class='is-admin' v-if='!currentUser.isAdmin'>You don't have permission to access this page</div>
		<div v-else>

		</div>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';
	import {mapActions, mapGetters} from 'vuex';

	export default {
		name: 'Admin',
		components: {Navbar},
		data() {
			return {
				users: []
			}
		},
		computed: {
			...mapGetters([
				'currentUser',
			])
		},
		mounted() {
			// There should be a better place for this
			api.getAllUsers()
				.then(response => {
					this.users = response.data;
				})
				.catch(() => {
					this.$toastr.e('Ups... Something went wrong during users fetch');
				})
		}
	}
</script>

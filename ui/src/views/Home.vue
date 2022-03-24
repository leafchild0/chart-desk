<template>
	<div class='home'>
		<Navbar/>
		<p>This is Home</p>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';

	export default {
		name: 'Home',
		components: {Navbar},
		mounted() {
			// There should be a better place for this
			api.getCurrentUser()
				.then(response => {
					this.$store.dispatch('updateUser', response.data);
				})
				.catch(() => {
					this.$toastr.e('Ups... Something went wrong during user fetch');
				})
		}
	}
</script>

<template>
	<div class='chart'>
		<Navbar/>
		<ChartsDetails :chart='chart' :tags='tags' :username='currentUserName' v-on:tag-add='addTag' v-on:tag-assign='assignTag' v-on:tag-unassign='unassignTag'>
		</ChartsDetails>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';
	import ChartsDetails from '@/components/ChartsDetails';

	export default {
		name: 'Chart',
		components: {
			ChartsDetails,
			Navbar,
		},
		data() {
			return {
				chart: {},
				tags: [],
				currentUserName: ''
			}
		},
		computed: {
		},
		methods: {
			addTag(payload) {
				api.createTag(payload.tag.name)
					.then(response => {
						payload.tag.id = response.data.id
						return this.assignTag(payload);
					})
			},
			assignTag(payload) {
				const assignPayload = {
					chartNames: Array.of(payload.name),
					// TODO: get username here
					userName: '2'
				}
				api.assignTag(payload.tag.id, assignPayload);
			},
			unassignTag(payload) {
				const assignPayload = {
					chartNames: Array.of(payload.name),
					// TODO: get username here
					userName: '2'
				}
				api.unassignTag(payload.tag.id, assignPayload);
			}
		},
		mounted() {
			if (this.$route.params.id) {
				api.getChart(this.$route.params.id).then((response) => {
					this.chart = response.data
				}).catch(() => {
					this.$toastr.e('Something went wrong while getting chart');
				});

				api.tagList().then((response) => {
					this.tags = response.data;
				}).catch(() => {
					this.$toastr.e('Something went wrong while getting tags');
				}).finally(() => this.loading = false);

				api.getCurrentUser().then(response => {
					this.currentUserName = response.data.username;
				}).catch(() => {
					this.$toastr.e('Ups... Something went wrong during user fetch');
				});
			}
		}
	}
</script>

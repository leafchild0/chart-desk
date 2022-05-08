<template>
	<div class='chart'>
		<Navbar/>
		<ChartsDetails
			:chart='chart'
			:versions='versions'
			:tags='tags'
			:tag-add='addTag'
			:tag-assign='assignTag'
			:tag-unassign='unassignTag'
			@version-change='versionChanged'>
		</ChartsDetails>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';
	import ChartsDetails from '@/components/ChartsDetails';
	import {mapGetters} from 'vuex';

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
				versions: []
			}
		},
		computed: {
			...mapGetters([
				'currentUser',
			])
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
			},
			getTags() {
				api.tagList().then((response) => {
					this.tags = response.data;
				}).catch(() => {
					this.$toastr.e('Something went wrong while getting tags');
				});
			},
			versionChanged(id) {
				this.loading = true;
				this.getChart(id).finally(() => this.loading = false);
			},
			getChart(id) {
				return api.getChart(this.currentUser.username, id).then(response => {
					this.chart = response.data;
				}).catch(() => {
					this.$toastr.e('Something went wrong while getting chart');
				});
			},
			getChartVersions() {
				api.getAllChartVersions(this.currentUser.username, this.chart.name).then((response) => {
					this.versions = response.data;
				}).catch(() => {
					this.$toastr.e('Something went wrong while getting versions');
				});
			}
		},
		mounted() {
			if (this.$route.params.id) {
				try {
					this.getChart(this.$route.params.id).then(() => {
						this.getChartVersions();
						this.getTags();
					});
				} finally {
					this.loading = false
				}
			}
		}
	}
</script>

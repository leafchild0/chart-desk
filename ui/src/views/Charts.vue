<template>
	<div class='charts'>
		<Navbar/>
		<UploadChartButton v-on:upload-chart='uploadChart' :title='"Upload chart (.tar.gz)"' :format='".tgz"'/>
		<FilterableTable :data='charts' :filter-columns='filterColumns' :headers='headers' :loading='loading' :tags='tags'
		v-on:tag-add='addTag' v-on:tag-assign='assignTag' v-on:tag-unassign='unassignTag'>
			<template v-slot:actions='props'>
				<b-tooltip label='View details' position='is-left' type='is-info'>
					<b-button size='is-small' type='is-primary' icon-left='format-list-bulleted' @click='() => showDetails(props.id)'></b-button>
				</b-tooltip>
			</template>
		</FilterableTable>
	</div>
</template>

<script>

	import FilterableTable from '@/components/FilterableTable';
	import Navbar from '@/components/Navbar';
	import UploadChartButton from '@/components/UploadChartButton';
	import api from '@/api';

	export default {
		name: 'Charts',
		components: {
			Navbar,
			UploadChartButton,
			FilterableTable
		},
		data() {
			return {
				charts: [],
				tags: [],
				loading: true,
				headers: [
					{field: 'name', label: 'Chart name'},
					{field: 'versions', label: 'Chart versions'},
					{field: 'tags', label: 'Tags'},
					{field: 'created', label: 'Date'},
				]
			}
		},
		computed: {
			filterColumns() {
				return this.headers.map(h => h.field)
			}
		},
		methods: {
			uploadChart(formData) {
				api.uploadChart(formData).then((response) => {
					if (response.status === 201) {
						this.$toastr.s('Helm chart ' + response.data.name + ', version: ' + response.data.version + ' was uploaded.');
					}
				}).catch(() => {
					this.$toastr.e('Something went wrong while uploading chart.')
				});
			},
			showDetails(id) {
				this.$router.push({name: 'chart', params: {id: id}})
			},
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
			api.chartsList().then((response) => {
				this.charts = response.data;
			}).catch(() => {
				this.$toastr.e('Something went wrong while getting charts');
			}).finally(() => this.loading = false);

			api.tagList().then((response) => {
				this.tags = response.data;
			}).catch(() => {
				this.$toastr.e('Something went wrong while getting tags');
			}).finally(() => this.loading = false);
		}
	}
</script>

<style scoped lang='scss'>

	.charts {
	}

</style>

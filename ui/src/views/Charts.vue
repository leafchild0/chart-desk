<template>
	<div class='charts'>
		<Navbar/>
		<div class='controls'>
			<b-button
				id='upload-modal-button'
				label='Upload chart'
				type='is-primary' @click='isUploadModalActive = true'/>
			<b-button
				id='third-party-import-modal-button'
				label='Third-party import'
				type='is-primary' @click='isThirdpartyModalActive = true'/>
			<b-modal
				v-model='isUploadModalActive'
				has-modal-card
				trap-focus
				:destroy-on-hide='false'
				aria-role='dialog'
				close-button-aria-label='Close'
				aria-modal>
				<template>
					<div class='modal-card' style='width: auto'>
						<header class='modal-card-head'>
							<p class='modal-card-title'>Upload Chart</p>
						</header>
						<section class='modal-card-body'>
							<UploadChartButton v-on:upload-chart='uploadChart' :title='"Click to select chart (.tar.gz)"' :format='".tgz"'/>
						</section>
					</div>
				</template>
			</b-modal>

			<b-modal
				v-model='isThirdpartyModalActive'
				has-modal-card
				trap-focus
				:destroy-on-hide='false'
				aria-role='dialog'
				close-button-aria-label='Close'
				aria-modal>
				<template>
					<ThirdPartImport
						:data='pulledCharts'
						@pull-charts='pullCharts'
						@cancel='isThirdpartyModalActive = false'
						@upload='uploadCharts'
					/>
				</template>
			</b-modal>
		</div>
		<FilterableTable
			:data='charts'
			:filter-columns='filterColumns'
			:headers='headers' :loading='loading'
			:tags='tags'
			:tag-add='addTag'
			:tag-assign='assignTag'
			:tag-unassign='unassignTag'>
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
	import ThirdPartImport from '@/components/ThirdPartImport';
	import api from '@/api';
	import {mapGetters} from 'vuex';

	export default {
		name: 'Charts',
		components: {
			ThirdPartImport,
			Navbar,
			UploadChartButton,
			FilterableTable
		},
		data() {
			return {
				charts: [],
				tags: [],
				loading: true,
				isUploadModalActive: false,
				isThirdpartyModalActive: false,
				pulledCharts: [],
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
			},
			...mapGetters([
				'currentUser',
			]),
		},
		watch: {
			currentUser(newUser) {
				// Get charts on user loaded, do not use in production
				this.loadCharts(newUser.username);
			}
		},
		methods: {
			uploadChart(formData) {
				api.uploadChart(this.currentUser.username, formData).then((response) => {
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
			pullCharts(url) {
				api.pullCharts({thirdPartyUrl: url}).then((response) => {
					this.pulledCharts = [];
					response.data.forEach(chart => {
						this.pulledCharts.push({
							text: chart.name,
							children: chart.versions.map(version => {
								return {
									text: ' version ' + version,
									data: {
										version,
										name: chart.name
									}
								}
							})
						})
					})
				}).catch(() => {
					this.$toastr.e('Something went wrong while pulling charts list from url')
				});

			},
			uploadCharts(payload) {

				api.uploadCharts(
					this.currentUser.username,
					{
						thirdPartyUrl: payload.url,
						entries: payload.charts
					})
					.then((response) => {
						if (response.status === 200) {
							this.isThirdpartyModalActive = false;
							this.$toastr.s('Charts were successfully uploaded');
						}
					}).catch(() => {
						this.$toastr.e('Something went wrong while uploading chart')
					});
			},
			loadCharts(username) {
				if (username) {
					return api.chartsList(username).then((response) => {
						this.charts = response.data;
					}).catch(() => {
						this.$toastr.e('Something went wrong while getting charts');
					}).finally(() => this.loading = false);
				}
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
					userName: this.currentUser.username
				}
				api.assignTag(payload.tag.id, assignPayload);
			},
			unassignTag(payload) {
				const assignPayload = {
					chartNames: Array.of(payload.name),
					userName: this.currentUser.username
				}
				api.unassignTag(payload.tag.id, assignPayload);
			}
		},
	}
</script>

<style scoped lang='scss'>

	.charts {

		.controls {
			display: flex;
			justify-content: end;
			margin: 15px
		}

		#upload-modal-button {
			margin-right: 5px;
		}
	}

</style>

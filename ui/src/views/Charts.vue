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
						@pull='pullCharts'
						@cancel='isThirdpartyModalActive = false'
					/>
				</template>
			</b-modal>
		</div>
		<FilterableTable v-if='Object.keys(charts).length' :data='charts' :filter-columns='filterColumns' :headers='headers' :loading='loading'>
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
				loading: true,
				isUploadModalActive: false,
				isThirdpartyModalActive: false,
				pulledCharts: [
					{text: 'Item 1'},
					{
						text: 'Item 2', children: [
							{text: 'Item 2.1'},
							{text: 'Item 2.2'},
							{text: 'Item 2.3'}
						]
					},
					{
						text: 'Item 3', children: [
							{text: 'Item 3.1'},
							{text: 'Item 3.2'},
							{text: 'Item 3.3'}
						]
					},
					{text: 'Item 4'}
				],
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
				this.loadCharts(newUser.id);
			}
		},
		methods: {
			uploadChart(formData) {
				api.uploadChart(this.currentUser.id, formData).then((response) => {
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
				console.log(url)
			},
			loadCharts(userId) {
				if (userId) {
					return api.chartsList(userId).then((response) => {
						this.charts = response.data;
					}).catch(() => {
						this.$toastr.e('Something went wrong while getting charts');
					}).finally(() => this.loading = false);
				}
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

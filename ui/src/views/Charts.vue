<template>
	<div class='charts'>
		<Navbar/>
		<UploadChartButton v-on:upload-chart='uploadChart' :title='"Upload chart (.tar.gz)"' :format='".tgz"'/>
		<FilterableTable :data='charts' :filter-columns='filterColumns' :headers='headers' :details='true' :loading='loading'>
			<template v-slot:details='props'>
				<ChartsDetails :chart='props.row'/>
			</template>
		</FilterableTable>
	</div>
</template>

<script>

	import FilterableTable from '@/components/FilterableTable';
	import Navbar from '@/components/Navbar';
	import UploadChartButton from '@/components/UploadChartButton';
	import api from '@/api';
	import ChartsDetails from '@/components/ChartsDetails';

	export default {
		name: 'Charts',
		components: {
			ChartsDetails,
			Navbar,
			UploadChartButton,
			FilterableTable
		},
		data() {
			return {
				charts: [],
				loading: true,
				headers: [
					{field: 'name', label: 'Chart name'},
					{field: 'version', label: 'Chart version'},
					{field: 'description', label: 'Description'},
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
			}
		},
		mounted() {
			api.chartsList().then((response) => {
				this.charts = response.data;
			}).catch(() => {
				this.$toastr.e('Something went wrong while getting charts');
			}).finally(() => this.loading = false);
		}
	}
</script>

<style scoped lang='scss'>

	.charts {
	}

</style>

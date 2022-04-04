<template>
	<div class='charts'>
		<Navbar/>
		<UploadChartButton :format='".tgz"'/>
		<FilterableTable :data='charts' :filter-columns='filterColumns' :headers='headers'/>
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
		methods: {},
		data() {
			return {
				charts: [],
				headers: [
					{field: 'name', label: 'Chart name'},
					{field: 'version', label: 'Chart version'},
					{field: 'description', label: 'Description'},
					{field: 'created', label: 'Date'},
				]
			}
		},
		mounted() {
			api.chartsList().then((response) => {
				this.charts = [].concat(...Object.values(response.data.entries));
			}).catch(() => {
				this.$toastr.e('Something went wrong while getting charts');
			});
		},
		computed: {
			filterColumns() {
				return this.headers.map(h => h.field)
			}
		}
	}
</script>

<template>
	<div class='charts'>
		<Navbar/>
		<UploadChartButton :format='".tgz"'/>
		<ChartTable :charts='data'/>
	</div>
</template>

<script>

	import ChartTable from '@/components/ChartTable';
	import Navbar from '@/components/Navbar';
	import UploadChartButton from '@/components/UploadChartButton';
	import api from '@/api';

	export default {
		name: 'Charts',
		components: {
			Navbar,
			UploadChartButton,
			ChartTable
		},
		methods: {},
		data() {
			return {
				data: [],
			}
		},
		mounted() {
			api.chartsList().then((response) => {
				this.data = [].concat(...Object.values(response.data.entries));
			}).catch(() => {
				this.$toastr.e('Something went wrong while getting charts');
			});
		}
	}
</script>

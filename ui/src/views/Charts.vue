<template>
	<div class='charts'>
		<Navbar/>
		<UploadChartButton/>
        <ChartTable/>
  </div>
</template>

<script>

	import ChartTable from '@/components/ChartTable';
	import Navbar from '@/components/Navbar';
	import UploadChartButton from '@/components/UploadChartButton';

	export default {
		name: 'Charts',
		components: {
			Navbar,
			UploadChartButton,
			ChartTable
		}
	}
</script>

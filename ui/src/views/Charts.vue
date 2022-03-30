<template>
	<div class='charts'>
		<UploadChartButton/>
        <ChartTable/>
  </div>
</template>

<script>

	import ChartTable from '@/components/ChartTable';
	import UploadChartButton from '@/components/UploadChartButton';

	export default {
		name: 'Charts',
		components: {
			UploadChartButton,
			ChartTable
		}
	}
</script>

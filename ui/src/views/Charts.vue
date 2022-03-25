<template>
	<div class='charts'>
		<Navbar/>
		<ChartTable/>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import ChartTable from '@/components/ChartTable';

  export default {
		name: 'Charts',
		components: {
			Navbar,
			ChartTable
		}
	}
</script>

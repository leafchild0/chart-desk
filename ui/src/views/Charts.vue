<template>
	<div class='charts'>
		<Navbar/>
        <ChartTable/>
  </div>
</template>

<script>

	import ChartTable from '@/components/ChartTable';
	import Navbar from '@/components/Navbar';

	export default {
		name: 'Charts',
		components: {
			Navbar,
			ChartTable
		}
	}
</script>

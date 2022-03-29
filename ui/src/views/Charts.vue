<template>
	<div class='charts'>
		<HeaderMenu/>
        <ChartTable/>
  </div>
</template>

<script>

	import HeaderMenu from '@/components/HeaderMenu';
	import ChartTable from '@/components/ChartTable';

	export default {
		name: 'Charts',
		components: {
			HeaderMenu,
			ChartTable
		}
	}
</script>

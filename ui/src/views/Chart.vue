<template>
	<div class='chart'>
		<Navbar/>
		<ChartsDetails :chart='chart'></ChartsDetails>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';
	import api from '@/api';
	import ChartsDetails from '@/components/ChartsDetails';

	export default {
		name: 'Chart',
		components: {
			ChartsDetails,
			Navbar,
		},
		data() {
			return {
				chart: {},
			}
		},
		computed: {
		},
		methods: {
		},
		mounted() {
			if (this.$route.params.id) {
				api.getChart('2', this.$route.params.id).then((response) => {
					this.chart = response.data
				}).catch(() => {
					this.$toastr.e('Something went wrong while getting chart');
				});
			}
		}
	}
</script>

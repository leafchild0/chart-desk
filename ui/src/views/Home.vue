<template>
	<div class='home'>
		<Navbar/>
		<p>This is Home</p>
	</div>
</template>

<script>

	import Navbar from '@/components/Navbar';

	export default {
		name: 'Home',
		components: {Navbar},
	}
</script>

<template>
	<div class='home'>
		<Navbar/>
		<p>This is Home</p>
	</div>
</template>

<script>

	import Navbar from '@/components/HeaderMenu';
	export default {
		name: 'Home',
		components: {Navbar}
	}
</script>

<template>
  <div id='app'>
    <section v-if='isLoggedIn'>
      <b-breadcrumb align='is-centered'>
        <b-breadcrumb-item href='/'>Home</b-breadcrumb-item>
        <b-breadcrumb-item href='/charts'>Charts</b-breadcrumb-item>
        <b-breadcrumb-item href='/about'>About</b-breadcrumb-item>
      </b-breadcrumb>
    </section>
    <router-view/>
  </div>
</template>

<script>

	export default {
		name: 'App',
		props: ['isLoggedIn']
	}
</script>

<style lang="scss">
#app {
  font-family: Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>

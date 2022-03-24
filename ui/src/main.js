import Vue from 'vue'
import App from './App.vue'
import Buefy from 'buefy'
import './App.scss'
import Toastr from 'vue-toastr'
import store from './store'
import router from './router'

Vue.config.productionTip = false
Vue.use(Buefy)

Vue.use(Toastr, {
	defaultPosition: 'toast-bottom-center',
	defaultProgressBar: false
})

// Check token in session storage
await store.dispatch('checkAndPopulateToken')

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')

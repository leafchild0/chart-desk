import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Buefy from 'buefy'
import 'buefy/dist/buefy.css'
import Toastr from 'vue-toastr'
import tokenManager from '@/auth/tokenManager'
import Vuex from 'vuex'

Vue.config.productionTip = false
Vue.use(Buefy)
Vue.use(Vuex)

Vue.use(Toastr, {
	defaultPosition: 'toast-bottom-center',
	defaultProgressBar: false
})

// Check token in session storage
tokenManager.checkAndPopulateToken()

new Vue({
	router,
	render: h => h(App)
}).$mount('#app')

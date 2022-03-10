import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Charts from '../views/Charts.vue'
import SignUp from '@/views/SignUp'
import Login from '@/views/Login'
import tokenManager from '@/auth/tokenManager'

Vue.use(VueRouter)

const routes = [
	{
		path: '/signup',
		name: 'signup',
		component: SignUp
	},
	{
		path: '/login',
		name: 'login',
		component: Login
	},
	{
		path: '/',
		name: 'Home',
		component: Home,
		meta: {
			requiresAuth: true
		}
	},
	{
		path: '/charts',
		name: 'Charts',
		component: Charts,
		meta: {
			requiresAuth: true
		}
	},
	{
		path: '/about',
		name: 'About',
		meta: {
			requiresAuth: true
		},
		// route level code-splitting
		// this generates a separate chunk (about.[hash].js) for this route
		// which is lazy-loaded when the route is visited.
		component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
	},
	// All unknown routes should go to Login
	{
		path: '*',
		redirect: '/login'
	}
]

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

router.beforeEach((to, from, next) => {

	// We can just check if token is there
	const currentUser = tokenManager.getToken();
	const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

	if (requiresAuth && !currentUser) {
		next('login')
	} else if (!requiresAuth && currentUser) {
		next('home')
	} else {
		next()
	}
});

export default router
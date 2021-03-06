import Vue from 'vue'
import VueRouter from 'vue-router'
import Charts from '../views/Charts.vue'
import SignUp from '@/views/SignUp'
import Login from '@/views/Login'
import store from '@/store'
import Admin from '@/views/Admin';
import Chart from '@/views/Chart';

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
		name: 'charts',
		component: Charts,
		meta: {
			requiresAuth: true
		}
	},
	{
		path: '/chart/:id',
		name: 'chart',
		component: Chart,
		props: true,
		meta: {
			requiresAuth: true
		}
	},
	{
		path: '/admin',
		name: 'admin',
		component: Admin,
		meta: {
			requiresAuth: true,
			isAdmin: true
		}
	},
	{
		path: '/about',
		name: 'about',
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

	if (to.path === '/login' || to.path === '/signup') next();

	// We can just check if token is there
	const isLoggedIn = store.getters['userToken'] !== '';
	const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

	if (requiresAuth && !isLoggedIn) {
		next('login')
	} else if (!requiresAuth && isLoggedIn) {
		next('home')
	} else {
		next()
	}
});

export default router

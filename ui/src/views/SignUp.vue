<template>
	<div class='signup'>
		<section class='section'>
			<div class='container'>
				<div class='modal-card'>
					<header class='modal-card-head'>
						<p class='modal-card-title'>Sign Up</p>
					</header>
					<section class='modal-card-body'>
						<b-field label='Username'>
							<b-input
								type='text'
								v-model='username'
								placeholder='Username'
								maxlength='15'
								validation-message='Only lowercase letter and numbers are allowed'
								pattern='[a-z0-9]*'
								required>
							</b-input>
						</b-field>

						<b-field label='First Name'>
							<b-input
								type='text'
								v-model='firstName'
								placeholder='First Name'
								maxlength='30'
								required>
							</b-input>
						</b-field>

						<b-field label='Last Name'>
							<b-input
								type='text'
								v-model='lastName'
								placeholder='Last Name'
								maxlength='30'
								required>
							</b-input>
						</b-field>

						<b-field label='Password'>
							<b-input
								type='password'
								v-model='password'
								password-reveal
								min='8'
								validation-message='Min 6 characters, at least one lower case and one upper case'
								pattern='(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{6,20}'
								placeholder='Your password'
								required>
							</b-input>
						</b-field>
						<b-field label='Confirm Password'
									:type='{ "is-danger": !passwordsSame, "password": passwordsSame  }'
									:message='{ "Passwords should be the same": !passwordsSame }'>
							<b-input
								v-model='confirmPassword'
								password-reveal
								placeholder='Confirm password'
								required>
							</b-input>
						</b-field>
					</section>
					<footer class='modal-card-foot'>
						<b-button
							label='Sign Up'
							type='is-info' @click='signUp' :disabled='!isValid'/>
						<b-button type='is-text' @click='goToLogin'>
							Have account? Login
						</b-button>
					</footer>
				</div>
			</div>
		</section>
	</div>
</template>

<script>
	import authApi from '../auth/authApi';

	export default {
		name: 'Signup',
		data: function () {
			return {
				password: '',
				username: '',
				firstName: '',
				lastName: '',
				confirmPassword: '',
			}
		},
		computed: {
			isValid() {
				return this.username !== ''
					&& this.password !== ''
					&& this.firstName !== ''
					&& this.lastName !== ''
					&& this.confirmPassword !== ''
			},
			passwordsSame() {
				return this.password === this.confirmPassword
			}
		},
		methods: {
			signUp: function () {
				if (this.isValid) {
					authApi
						.post('auth/signup', {
							username: this.username,
							password: this.password,
							firstName: this.firstName,
							lastName: this.lastName,
						})
						.then(
							() => {
								this.$router.replace('login');
							},
							(error) => {
								if (error.response.status === 400
									&& error.response.data.message === 'Username is already in use') {
									this.$toastr.e('Username is already in use');
								} else {
									this.$toastr.e('Ups... Something went wrong');
								}
							}
						);
				}
			},
			goToLogin() {
				this.$router.replace('login');
			}
		}
	};
</script>

<style scoped lang="scss">
.signup {
	display: block;
	height: 100%;
	width: 100%;
}
</style>

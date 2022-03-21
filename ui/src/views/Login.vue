<template>
	<div class='login'>
    <section class='section'>
		<div class='container'>
      <div class='modal-card login-card'>
        <header class='modal-card-head'>
          <p class='modal-card-title'>Login</p>
        </header>
        <section class='modal-card-body'>
          <b-field label='Username'>
            <b-input
                type='text'
                v-model='username'
                placeholder='Your username'
                required>
            </b-input>
          </b-field>

          <b-field label='Password'>
            <b-input
                type='password'
                v-model='password'
                password-reveal
                placeholder='Your password'
                required>
            </b-input>
          </b-field>
        <!--Commented for now <b-checkbox>Remember me</b-checkbox>-->
        </section>
        <footer class='modal-card-foot'>
          <b-button
              label='Login'
              type='is-primary' @click='login' :disabled='!isValid'/>
          <b-button type='is-text' @click='goToSignUp'>
            New? Sign up
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
		name: 'Login',
		data: function() {
			return {
				username: '',
				password: '',
			};
		},
		computed: {
			isValid() {
				return this.username !== '' && this.password !== '';
			}
		},
		methods: {
			login: function() {
				const self = this;

				if (this.isValid) {
					authApi.post('api/auth/login', {
						username: this.username,
						password: this.password
					})
						.then(response => {
								this.$store.dispatch('setToken', response.data.accessToken);
								self.$router.replace('/');
							},
							err => {
								if (err.response?.status === 401) {
									self.$toastr.e('Username or Password is incorrect');
								} else {
									self.$toastr.e('Ups... Something went wrong');
								}
							}
						);
				}
			},
			goToSignUp() {
				this.$router.replace('signup');
			}
		}
	};
</script>

<style scoped lang="scss">

	.login {
		display: block;
		height: 100%;
		width: 100%;
	}

	.login-card {
		border: 1px solid lightgray;
		border-radius: 4px;
		width: 40%;
	}
</style>

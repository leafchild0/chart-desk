<template>
	<div class='user-account'>
		<div class='container'>
			<div class='modal-card login-card'>
				<header class='modal-card-head'>
					<p class='modal-card-title'>User Account</p>
				</header>
				<section class='modal-card-body'>
					<b-tabs v-model='activeTab'>
						<b-tab-item label='General'>
							<b-field label='Username'>
								<b-input
									type='text'
									v-model='usernameM'
									placeholder='Username'
									maxlength='15'
									validation-message='Only lowercase letter and numbers are allowed'
									pattern='[a-z0-9]*'
									required>
								</b-input>
							</b-field>

							<b-field label='Email'>
								<b-input
									type='email'
									v-model='emailM'
									placeholder='Email'
									required>
								</b-input>
							</b-field>

							<b-field label='First Name'>
								<b-input
									type='text'
									v-model='firstNameM'
									placeholder='First Name'
									maxlength='30'
									required>
								</b-input>
							</b-field>

							<b-field label='Last Name'>
								<b-input
									type='text'
									v-model='lastNameM'
									placeholder='Last Name'
									maxlength='30'
									required>
								</b-input>
							</b-field>
						</b-tab-item>
						<b-tab-item label='Password'>
							<b-field label='Old Password'>
								<b-input
									type='password'
									v-model='oldPassword'
									password-reveal
									validation-message='Min 6 characters, at least one lower case and one upper case'
									pattern='(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{6,20}'
									required>
								</b-input>
							</b-field>
							<b-field label='New Password'>
								<b-input
									type='password'
									v-model='newPassword'
									password-reveal
									validation-message='Min 6 characters, at least one lower case and one upper case'
									pattern='(?=.*[0-9])(?=.*[a-zA-Z])(?=\S+$).{6,20}'
									placeholder='New password'
									required>
								</b-input>
							</b-field>
							<b-field label='Confirm Password'
									:type='{ "is-danger": !passwordsSame, "password": passwordsSame  }'
									:message='{ "Passwords should be the same": !passwordsSame }'>
								<b-input
									v-model='confirmPassword'
									type='password'
									password-reveal
									placeholder='Confirm password'
									required>
								</b-input>
							</b-field>
						</b-tab-item>
					</b-tabs>
				</section>
				<footer class='modal-card-foot'>
					<b-button
						label='Save'
						type='is-primary' @click='saveData'/>
					<b-button
						label='Close'
						@click='$emit("close")'/>
				</footer>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'UserAccount',
		props: ['email', 'firstName', 'lastName', 'username'],
		data() {
			return {
				isComponentModalActive: false,
				activeTab: 0,
				newPassword: '',
				oldPassword: '',
				usernameM: this.username,
				firstNameM: this.firstName,
				lastNameM: this.lastName,
				emailM: this.email,
				confirmPassword: '',
			}
		},
		computed: {
			isUserInfoValid() {
				return this.username !== ''
					&& this.password !== ''
					&& this.firstName !== ''
					&& this.lastName !== ''
			},
			passwordsSame() {
				return this.newPassword === this.confirmPassword
			}
		},
		methods: {
			saveData() {
				if (this.activeTab === 0) {
					if (this.isUserInfoValid) {
						// Save user info
						this.$emit('save', {
							username: this.usernameM,
							firstName: this.firstNameM,
							lastName: this.lastNameM,
							email: this.emailM
						})
					}
				} else if (this.activeTab === 1) {
					// Save password
					// Validations
					if (this.passwordsSame) {
						this.$emit('change-password',
							{oldPassword: this.oldPassword, password: this.newPassword});
					}
				} else {
					this.$toastr.e('Oops! Something went wrong');
				}
			}
		}
	}
</script>

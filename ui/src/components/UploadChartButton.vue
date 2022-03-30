<template>
	<div>
		<div class='upload-chart'>
			<b-field class='file is-primary' :class='{"has-name": !!chartFile}'>
				<b-upload @change='onChange' v-model='chartFile' class='file-label' accept='.tgz' required validationMessage='Please select a file'>
						<span class='file-cta'>
							<b-icon class='file-icon' icon='upload'></b-icon>
							<span class='file-label'>Click to upload chart (Only .tar.gz)</span>
						</span>
					<span class='file-name'>
							{{ chartFile ? chartFile.name : '' }}
						</span>
				</b-upload>
			</b-field>
			<b-button
				class='save-button'
				label='Save'
				type='is-primary' @click='onUpload'/>
		</div>
	</div>
</template>

<script>
	import chartsApi from '@/api/chartsApi';

	export default {
		data() {
			return {
				chartFile: null,
			}
		},
		methods: {
			onChange(event) {
				this.chartFile = event.target.files[0]
			},
			onUpload() {
				const formData = new FormData()
				formData.append('chart', this.chartFile, this.chartFile.name)
				chartsApi.post('api/2/charts', formData, {}).then((response) => {
					if (response.status === 201) {
						this.$toastr.s('Helm chart ' + response.data.name + ', version: ' + response.data.version + ' was uploaded.');
					}
				}).catch(() => {
					this.$toastr.e('Something went wrong while uploading chart.')
				});
			}
		}
	}
</script>

<style>
	.upload-chart {
		margin: 15px 15px;
		justify-content: center;
		display: flex;
	}

	.file-name {
		width: 250px;
	}
</style>

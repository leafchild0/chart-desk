<template>
	<div>
		<div class='upload-chart'>
			<b-field class='file is-primary' :class='{"has-name": !!chartFile}'>
				<b-upload @change='onChange' v-model='chartFile' class='file-label' :accept='format' required validationMessage='Please select a file'>
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

	export default {
		name: 'UploadChartButton',
		props: {
			format: String
		},
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
				const formData = new FormData();
				formData.append('chart', this.chartFile, this.chartFile.name);
				this.$emit('upload-chart', formData);
			}
		}
	}
</script>

<style lang='scss' scoped>
	.upload-chart {
		margin: 15px 15px;
		justify-content: center;
		display: flex;
	}

	.file-name {
		width: 250px;
	}
</style>

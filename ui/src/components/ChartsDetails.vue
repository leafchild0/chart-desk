<template>
	<div class='chart-details'>
		<article class='media'>
			<figure class='media-left'>
				<p class='image is-128x128'>
					<img :src='chart.icon'>
				</p>
			</figure>
			<div class='media-content'>
				<div class='content'>
					<div class='subtitle is-4'>{{ chart.name }}</div>
					<div class='field is-horizontal'>
						<div class='field-label is-normal'>
							<label class='label'>Versions</label>
						</div>
						<div class='field-body'>
							<div class='field'>
								<div class='control'>
									<span> version: <b-tag type='is-success'> {{ chart.version }}</b-tag></span>
									<span> app version: <b-tag type='is-success'> {{ chart.appVersion }}</b-tag></span>
								</div>
							</div>
						</div>
					</div>
					<div class='field is-horizontal'>
						<div class='field-label is-normal'>
							<label class='label'>Created</label>
						</div>
						<div class='field-body'>
							<div class='field'>
								<div class='control'>
									<span> {{ new Date(chart.created).toLocaleDateString() }}</span>
								</div>
							</div>
						</div>
					</div>
					<div class='other-data'>
						<div class='field is-horizontal'>
							<div class='field-label is-normal'>
								<label class='label'>Sources</label>
							</div>
							<div class='field-body'>
								<div class='field'>
									<div class='control'>
										<div class='sources' v-show='chart.sources' :key='source' v-for='source in chart.sources'>
											<a :href='source' target='_blank'>{{ source }}</a>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class='field is-horizontal'>
							<div class='field-label is-normal'>
								<label class='label'>Maintainers</label>
							</div>
							<div class='field-body'>
								<div class='field'>
									<div class='control'>
										<div class='maintainers' v-show='chart.maintainers' :key='m.name' v-for='m in chart.maintainers'>
											<span class='maintainer'>{{ `${m.name} - ${m.email}` }}</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class='field is-horizontal'>
							<div class='field-label is-normal'>
								<label class='label'>Urls</label>
							</div>
							<div class='field-body'>
								<div class='field'>
									<div class='control'>
										<div class='urls' v-show='chart.urls' :key='u' v-for='u in chart.urls'>
											<a :href='u' target='_blank'>{{ u }}</a>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class='media-right'>
				<div class='subtitle is-6'>Description</div>
				<div>{{ chart.description }}</div>
			</div>
		</article>
	</div>

</template>

<script>
	export default {
		name: 'ChartsDetails',
		props: {
			chart: {required: true}
		}
	}
</script>

<style scoped lang='scss'>
	.chart-details {
		.media {
			.media-left {
				align-self: center;
			}

			.subtitle {
				margin-top: 0;
			}

			.media-right {
				max-width: 400px;
			}
		}
	}

</style>

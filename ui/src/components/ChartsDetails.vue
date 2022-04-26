<template>
	<div class='chart-details'>
		<b-loading v-if='isLoading' :is-full-page='false' v-model='isLoading' :can-cancel='false'></b-loading>
		<article v-else>
			<header class='chart-header'>
				<p class='image is-128x128'>
					<img :src='chart.icon'>
				</p>
				<div class='subtitle is-2'>{{ chart.name }}</div>
			</header>
			<div class='divider'/>
			<div class='content'>
				<div class='field is-horizontal'>
					<div class='field-label is-normal'>
						<label class='label'>Versions</label>
					</div>
					<div class='field-body'>
						<div class='field'>
							<div class='control'>
								<span> version: <b-tag type='is-success'> {{ chart.version }}</b-tag></span>
								<span v-if='chart.appVersion'> app version: <b-tag type='is-success'> {{ chart.appVersion }}</b-tag></span>
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
			<div class='divider'/>
			<div class='chart-details'>
				<div class='subtitle is-4'>Description</div>
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
		},
		computed: {
			isLoading() {
				return Object.keys(this.chart).length === 0
			}
		}
	}
</script>

<style scoped lang='scss'>
	.chart-details {
		.chart-header {
			display: flex;
			justify-content: center;
			align-items: center;
		}
		.chart-details {

		}
		.subtitle {
			margin-top: 0;
		}

		.divider {
			padding-bottom: 15px;
			margin-bottom: 10px;
			border-bottom: 1px solid lightgray;
		}

	}
</style>

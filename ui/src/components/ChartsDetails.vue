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
			<div class='terminal space shadow'>
				<div class='top'>
					<div class='btns'>
						<span class='circle red'></span>
						<span class='circle yellow'></span>
						<span class='circle green'></span>
					</div>
					<div class='title'>helm terminal</div>
				</div>
				<pre class='body'>

# Add repository to the helm client
helm repo add {{ username }} http://localhost:8080/{{ username }}
# Fetch {{ chart.name }} chart
helm fetch {{ username }}/{{ chart.name }}
				</pre>
			</div>
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
							<label class='label'>Keywords</label>
						</div>
						<div class='field-body'>
							<div class='field'>
								<div class='control'>
									<ChartTags :all-tags='tags' :chart-name='chart.name' :chart-tags='chart.tags'>
									</ChartTags>
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
	import ChartTags from '@/components/ChartTags';

	export default {
		name: 'ChartsDetails',
		props: {
			chart: {required: true},
			tags: {required: true},
			username: {required: true}
		},
		components: {
			ChartTags
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

		.tags {
			display: flex;
			justify-content: center;
		}

		* { margin: 0; padding: 0; }
		.terminal {
			border-radius: 5px 5px 0 0;
			position: relative;

			.top {
				background: #E8E6E8;
				color: black;
				padding: 5px;
				border-radius: 5px 5px 0 0;
			}

			.btns {
				position: absolute;
				top: 4px;
				left: 5px;
			}

			.circle {
				width: 12px;
				height: 12px;
				display: inline-block;
				border-radius: 15px;
				margin-left: 2px;
				border-width: 1px;
				border-style: solid;
			}

			.body {
				background: black;
				color: #7AFB4C;
				padding: 8px;
				overflow: auto;
				text-align: left;
			}
		}
		.title{
			text-align: center;
			font-size: 15px;
		}
		.red { background: #EC6A5F; border-color: #D04E42; }
		.green { background: #64CC57; border-color: #4EA73B; }
		.yellow{ background: #F5C04F; border-color: #D6A13D; }
		.clear{clear: both;}
		.space {
			margin: 25px;
		}
		.shadow { box-shadow: 0px 0px 10px rgba(0,0,0,.4)}

	}
</style>

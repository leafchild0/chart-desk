<template>
	<div class='modal-card thirdparty'>
		<header class='modal-card-head'>
			<p class='modal-card-title'>Third-party Chart import</p>
		</header>
		<section class='modal-card-body'>
			<div class='field'>
				<b-field label='Import url' :label-position='"on-border"'>
					<b-input v-model='url' placeholder='Index url' type='url' expanded/>
					<p class='control'>
						<b-button class='button is-primary' :disabled='!url' @click='pullCharts'>Pull</b-button>
					</p>
				</b-field>
			</div>
			<div class='divider'/>
			<p class='tree-data'>Charts:</p>
			<div class='content'>
				<tree
					v-if='treeData.length > 0'
					ref='tree'
					:data='treeData'
					:options='options'
				/>
			</div>
		</section>
		<footer class='modal-card-foot'>
			<b-button
				label='Upload'
				@click='uploadCharts'
				:disabled='treeData.length === 0'
				type='is-primary'/>
			<b-button
				label='Cancel'
				@click='$emit("cancel")'/>
		</footer>
	</div>
</template>

<script>
	import LiquorTree from 'liquor-tree';

	export default {
		name: 'ThirdPartImport',
		props: ['data'],
		components: {
			[LiquorTree.name]: LiquorTree
		},
		data() {
			return {
				url: '',
				treeData: [],
				selected: [],
				options: {
					emptyText: 'No charts found!',
					checkbox: true
				}
			}
		},
		watch: {
			url() {
				this.treeData = [];
			},
			data(newValue) {
				this.treeData = newValue;
			}
		},
		methods: {
			uploadCharts() {
				const selectedNodes = this.$refs.tree.findAll({state: {checked: true}})
				this.selected = selectedNodes.filter(n => n.data.version).map(n => {
					return {
						name: n.data.name,
						versions: [n.data.version]
					}
				})

				this.$emit('upload', {
					url: this.url,
					charts: this.selected
				})
			},
			pullCharts() {
				this.$emit('pull-charts', this.url)
			}
		}
	}
</script>

<style lang='scss' scoped>

	.thirdparty {
		width: 100vm;

		.content {
			height: 400px;
		}

		.divider {
			padding-bottom: 15px;
			margin-bottom: 10px;
			border-bottom: 1px solid lightgray;
		}
	}

</style>

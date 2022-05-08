<template>
	<div class='tags' v-if='chartTagsData'>
		<tags-input element-id='tags'
					id-field='id'
					text-field='name'
					v-model='chartTagsData'
					:existing-tags='allTags'
					@tag-added='onTagAdded($event, chartName)'
					@tag-removed='onTagUnassign($event, chartName)'
					:typeahead='true'></tags-input>
	</div>
</template>

<script>
	import VoerroTagsInput from '@voerro/vue-tagsinput';

	export default {
		name: 'ChartTags',
		props: {
			chartName: {required: true},
			chartTags: {required: true},
			allTags: {required: true}
		},
		data: function() {
			return {
				chartTagsData: this.chartTags
			}
		},
		components: {
			'tags-input': VoerroTagsInput
		},
		computed: {
			isLoading() {
				return Object.keys(this.allTags).length === 0
			}
		},
		methods: {
			onTagAdded(tag, chartName) {
				const payload = {tag: tag, name: chartName};
				if (tag.id === '') {
					// create and assign new tag
					this.$parent.$emit('tag-add', payload);
				} else if (this.allTags.filter(t => t.name === tag.name).length !== 0) {
					// assign existing tag if not assigned previously
					this.$parent.$emit('tag-assign', payload);
				}
			},
			onTagUnassign(tag, chartName) {
				const payload = {tag: tag, name: chartName};
				this.$parent.$emit('tag-unassign', payload);
			}
		}
	}
</script>

<style scoped lang='scss'>
	.tags {
		display: flex;
		justify-content: center;
	}
</style>

<template>
	<div id='filterable-table'>
		<b-field label='Filter by' label-position='on-border' class='filter'>
			<b-input v-model='filterBy' placeholder='Filter by any field'></b-input>
		</b-field>
		<b-table
			striped
			paginated
			narrowed
			hoverable
			:loading='loading'
			per-page='30'
			:data='filteredData'
			:detailed='details'
			detail-key='name'
		>
			<template v-for='header in headers'>
				<b-table-column centered :key='header.field' v-if='header.field === "versions"' v-bind='header'>
					<template v-slot='props'>
						<div class='tags' v-if='props.row["versions"]'>
							<span class='tag is-primary' :key='version' v-for='version in props.row["versions"]'>
								{{ version }}
							</span>
						</div>
					</template>
				</b-table-column>
				<b-table-column centered :key='header.field' v-else-if='header.field === "tags"' v-bind='header'>
					<template v-slot='props'>
						<ChartTags :all-tags='tags' :chart-name='props.row["name"]' :chart-tags='props.row["tags"]'>
						</ChartTags>
					</template>
				</b-table-column>
				<b-table-column :key='header.field' v-else v-bind='header'>
					<template v-slot='props'>
						{{ props.row[header.field] }}
					</template>
				</b-table-column>
			</template>
			<b-table-column v-if='isActions' field='actions' label='Actions' centered v-slot='props'>
				<slot name='actions' v-bind='props.row'></slot>
			</b-table-column>

			<template #empty>
				<div class='has-text-centered'>No Data</div>
			</template>

			<template #detail='props'>
				<slot name='details' v-bind='props'></slot>
			</template>
		</b-table>
	</div>
</template>

<script>
	import ChartTags from '@/components/ChartTags';

	export default {
		name: 'FilterableTable',
		props: ['data', 'filterColumns', 'headers', 'details', 'loading', 'tags'],
		components: {
			ChartTags,
		},
		data() {
			return {
				filterBy: '',
			}
		},
		computed: {
			filteredData() {
				if (!this.filterBy) return this.data

				return this.data.filter(entry => {
					for (const f of this.filterColumns) {
						if (!entry[f]) return false
						if (entry[f].toString().includes(this.filterBy)) return true
					}
					return false
				})
			},
			isActions() {
				return this.$slots.actions || this.$scopedSlots.actions
			}
		}
	}
</script>

<style lang='scss'>

	@import '~@voerro/vue-tagsinput/dist/style.css';

	#filterable-table {

		padding: 15px;

		.filter {
			width: 200px;
		}

		.b-table div.top.level {
			justify-content: center;
		}

		.b-table .table th .th-wrap {
			justify-content: center;
			display: flex;
		}
	}

</style>

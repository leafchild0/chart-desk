<template>
	<div id='user-table'>
		<b-field label='Filter by' label-position='on-border' class='filter'>
			<b-input v-model='filterBy' placeholder='Filter by any field'></b-input>
		</b-field>
		<b-table striped paginated narrowed hoverable :loading='data.length === 0' per-page='30' :data='filteredData'>
			<template v-for='header in headers'>
				<b-table-column :key='header.field' v-bind='header'>
					<template v-slot='props'>
						{{ props.row[header.field] }}
					</template>
				</b-table-column>
			</template>
			<b-table-column field='actions' label='Actions' centered v-slot='props'>
				<slot name='actions' v-bind='props.row'></slot>
			</b-table-column>
		</b-table>
	</div>
</template>

<script>

	export default {
		name: 'UserTable',
		props: ['data', 'filterColumns', 'headers'],
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
						if (entry[f].includes(this.filterBy)) return true
					}
					return false
				})
			}
		}
	}
</script>

<style lang='scss'>

	#user-table {

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

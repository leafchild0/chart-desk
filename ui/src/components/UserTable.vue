<template>
	<div id='user-table'>
		<b-field label='Filter by' label-position='on-border' class='filter'>
			<b-input v-model='filterBy' placeholder='Filter by any field'></b-input>
		</b-field>
		<b-table striped paginated narrowed hoverable :loading='data.length === 0' per-page='30' :data='filteredData' :columns='headers'>
			<b-table-column class='has-text-success' field='actions' label='Actions' centered v-slot='props'>
				<b-tooltip label='Deactivate user' position='is-left' type='is-warning'>
					<b-button size='is-small' type='is-danger' outlined rounded icon-left='account-off' @click='deactivateUser(props.row.id)'></b-button>
				</b-tooltip>
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
		},
		methods: {
			deactivateUser(id) {
				this.$emit('deactivate', id)
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

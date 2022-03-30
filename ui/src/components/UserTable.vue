<template>
	<div id='user-table'>
		<b-table striped paginated narrowed hoverable :loading='data.length === 0' per-page='30' :data='data'>

			<b-table-column field='name' label='User name' centered sortable searchable v-slot='props'>
				{{ props.row.username }}
			</b-table-column>

			<b-table-column field='firstName' label='First Name' centered searchable v-slot='props'>
				{{ props.row.firstName }}
			</b-table-column>

			<b-table-column field='lastName' label='Last Name' centered searchable v-slot='props'>
				{{ props.row.lastName }}
			</b-table-column>

			<b-table-column field='email' label='Email' centered searchable v-slot='props'>
				{{ props.row.email }}
			</b-table-column>

			<b-table-column field='isAdmin' label='Admin' centered searchable v-slot='props'>
				{{ props.row.email }}
			</b-table-column>

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
		props: ['data'],
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

		.b-table div.top.level {
			justify-content: center;
		}

		.b-table .table th .th-wrap {
			justify-content: center;
			display: flex;
		}
	}

</style>

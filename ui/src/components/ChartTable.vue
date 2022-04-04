<template>
	<div>
		<b-table paginated per-page='30' :data='charts'>

			<b-table-column field='name' label='Chart name' centered sortable searchable v-slot='props'>
				{{ props.row.name }}
			</b-table-column>

			<b-table-column field='version' label='Chart version' centered searchable v-slot='props'>
				{{ props.row.version }}
			</b-table-column>

			<b-table-column field='description' label='Description' centered searchable v-slot='props'>
				{{ props.row.description }}
			</b-table-column>

			<b-table-column class='has-text-success' field='created' label='Date' centered searchable sortable v-slot='props'>
                <span class='tag is-success'>
                    {{ new Date(props.row.created).toLocaleString() }}
                </span>

			</b-table-column>
		</b-table>
	</div>
</template>

<script>

	export default {
		name: 'ChartTable',
		props: {
			charts: Array
		},
		methods: {

		}
	}
</script>

<style>

	.b-table div.top.level {
		justify-content: center;
	}

	.b-table .table th .th-wrap {
		justify-content: center;
		display: flex;
	}

</style>

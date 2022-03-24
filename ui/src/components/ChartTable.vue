<template>
  <div>
    <b-table paginated per-page="1" :data="data">

      <b-table-column field="name" label="Chart name" centered sortable searchable v-slot="props">
        {{ props.row.name }}
      </b-table-column>

      <b-table-column field="version" label="Chart version" centered searchable v-slot="props">
        {{ props.row.version }}
      </b-table-column>

      <b-table-column field="description" label="Description" centered searchable v-slot="props">
        {{ props.row.description }}
      </b-table-column>

      <b-table-column class="has-text-success" field="created" label="Date" centered searchable sortable v-slot="props">
                <span class="tag is-success">
                    {{ new Date(props.row.created).toLocaleString() }}
                </span>

      </b-table-column>
    </b-table>
  </div>
</template>

<script>

import chartsApi from "@/auth/chartsApi";

export default {
  name: 'ChartTable',
  data: () => ({
    data: [],
  }),
  methods: {
    getData() {
      // TODO: user id/name here & gateway
      return chartsApi.get("http://192.168.50.149:8080/2/index.json", {
            dataType: "json",
          })
          .then((response) => {
            Object.keys(response.data.entries)
                .forEach(k => this.data.push(...response.data.entries[k]));
          })
          .catch((err) => console.error(err));
    }
  },
  mounted() {
    this.getData();
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

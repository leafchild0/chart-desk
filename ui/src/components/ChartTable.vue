<template>
  <div>
    <b-table :data="data">

      <b-table-column field="name" label="Chart name" centered sortable v-slot="props">
        {{ props.row.name }}
      </b-table-column>

      <b-table-column field="version" label="Chart version" centered v-slot="props">
        {{ props.row.version }}
      </b-table-column>

      <b-table-column field="description" label="Description" centered sortable v-slot="props">
        {{ props.row.description }}
      </b-table-column>

      <b-table-column class="has-text-success" field="created" label="Date" centered sortable v-slot="props">
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

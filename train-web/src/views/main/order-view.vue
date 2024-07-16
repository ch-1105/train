<template>
  <div>
    {{order}}
  </div>
</template>

<script>
import { defineComponent } from 'vue';
  export default defineComponent({
    name: "order-view",
    setup() {
      const order = sessionStorage.getItem("dailyTrainTicket") || {};
      return {
        order
      };
    },
  });
</script>
<template>
  <a-layout>
    <component-header></component-header>
    <component-sider></component-sider>
    <a-layout-footer style="text-align: center">
      Ant Design ©2018 Created by Ant UED
    </a-layout-footer>
  </a-layout>
</template>

<script>
import { defineComponent } from 'vue';
import ComponentHeader from "@/components/component-header.vue";
import ComponentSider from "@/components/component-silder.vue";

export default defineComponent({
  name: "main-page",
  components: {ComponentSider, ComponentHeader},
  setup() {
    return {
    };
  },
});
</script>
<style scoped>
</style>
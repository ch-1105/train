<template>
  <a-layout>
    <component-header></component-header>

    <a-layout-content style="padding: 0 50px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb>
      <a-layout style="padding: 24px 0; background: #fff">
        <component-sider></component-sider>
        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          {{test}}
        </a-layout-content>
      </a-layout>
    </a-layout-content>
    <a-layout-footer style="text-align: center">
      Ant Design ©2018 Created by Ant UED
    </a-layout-footer>
  </a-layout>
</template>

<script>
import {defineComponent, ref} from 'vue';
import ComponentHeader from "@/components/component-header.vue";
import ComponentSider from "@/components/component-silder.vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "main-page",
  components: {ComponentSider, ComponentHeader},
  setup() {
    const test = ref(0) ;

    axios.get("/member/test").then(response => {
      let data = response.data;
      if (data.code === 200) {
        notification.success({ description: 'test成功！' });
        test.value = data.data;
      } else {
        notification.error({ description: data.message });
      }
    });


    return {
      test
    };
  },
});
</script>
<style scoped>
</style>
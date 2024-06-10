<script setup>

</script>

<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys2"
        v-model:openKeys="openKeys"
        mode="inline"
        style="height: 100%"
    >
      <a-sub-menu key="sub1">
        <template #title>
                <span>
                  <user-outlined />
                  subnav 1
                </span>
        </template>
        <a-menu-item key="/welcome">
          <router-link to="/welcome">
            welcome
          </router-link>
          </a-menu-item>
        <a-menu-item key="/passenger">
          <router-link to="/passenger">
            passenger
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import { ref } from 'vue';
import { defineComponent } from 'vue';

export default defineComponent({
  name: "component-sider",
  setup() {
    return {
      selectedKeys1 : ref(['2']),
    };
  },
});
</script>

<style scoped>

</style>


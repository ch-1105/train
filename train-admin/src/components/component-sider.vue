<script setup>

</script>

<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
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
        <a-menu-item key="/">
          <router-link to="/">
            welcome
          </router-link>
          </a-menu-item>
        <a-menu-item key="/station">
          <router-link to="/station">
            station
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train">
          <router-link to="/train">
            train
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train-station">
          <router-link to="/train-station">
            train-station
          </router-link>
        </a-menu-item>
        <a-menu-item key="/train-carriage">
          <router-link to="/train-carriage">
            train-carriage
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import store from "@/store";
import router from '@/router'

export default defineComponent({
  name: "the-sider-view",
  setup() {
    let member = store.state.member;
    const selectedKeys = ref([]);

    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    return {
      member,
      selectedKeys
    };
  },
});
</script>

<style scoped>

</style>


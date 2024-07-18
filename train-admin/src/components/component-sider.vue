<script setup>

</script>

<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        :openKeys="['member','train','daily']"
        mode="inline"
        style="height: 100%"
    >
        <a-menu-item key="/">
          <router-link to="/">
            welcome
          </router-link>
        </a-menu-item>
      <a-sub-menu key="member">
        <a-menu-item key="/member-ticket">
          <router-link to="/member-ticket">
            member-ticket
          </router-link>
        </a-menu-item>
      </a-sub-menu>
        <a-sub-menu key="train">
          <template #title>
                <span>
                  <user-outlined />
                  数据管理
                </span>
          </template>
          <a-menu-item key="/station">
            <router-link to="/station">
              站点管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/train">
            <router-link to="/train">
              火车管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/train-station">
            <router-link to="/train-station">
              火车车站
            </router-link>
          </a-menu-item>
          <a-menu-item key="/train-carriage">
            <router-link to="/train-carriage">
              车厢管理
            </router-link>
          </a-menu-item>
          <a-menu-item key="/train-seat">
            <router-link to="/train-seat">
              座位管理
            </router-link>
          </a-menu-item>
        </a-sub-menu>

      <a-sub-menu key="daily">
        <template #title>
                <span>
                  <user-outlined />
                  每日管理
                </span>
        </template>
        <a-menu-item key="/daily-train">
          <router-link to="/daily-train">
            每日火车
          </router-link>
        </a-menu-item>
        <a-menu-item key="/daily-train-ticket">
          <router-link to="/daily-train-ticket">
            每日余票
          </router-link>
        </a-menu-item>
        <a-menu-item key="/daily-train-station">
          <router-link to="/daily-train-station">
            每日火车站点
          </router-link>
        </a-menu-item>
        <a-menu-item key="/daily-train-carriage">
          <router-link to="/daily-train-carriage">
            每日车厢
          </router-link>
        </a-menu-item>
        <a-menu-item key="/daily-train-seat">
          <router-link to="/daily-train-seat">
            每日座位
          </router-link>
        </a-menu-item>
        <a-menu-item key="/sk-token">
          <router-link to="/sk-token">
            sk-token
          </router-link>
        </a-menu-item>
        <a-menu-item key="/confirm-order">
          <router-link to="/confirm-order">
            confirm-order
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


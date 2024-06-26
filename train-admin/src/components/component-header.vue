<script setup>

</script>

<template>
  <a-layout-header class="component-header" id="components-layout-demo-top-side" >
    <div class="logo" />
    <div>
      管理控制台
    </div>
    <div style="float:right;color:white;">
      您好: admin &nbsp; &nbsp;
      <router-link to="/">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
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
    </a-menu>
  </a-layout-header>

</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import store from "@/store";
import router from '@/router'



export default defineComponent({
  name: "the-header-view",
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
#components-layout-demo-top-side .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
</style>


<script setup>

</script>

<template>
  <a-layout-header class="component-header" id="components-layout-demo-top-side" >
    <div class="logo" />
    <div style="float:right;color:white;">
      您好:{{member.mobile}} &nbsp; &nbsp;
      <router-link to="/">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
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
    </a-menu>
  </a-layout-header>

</template>

<script>
import { ref } from 'vue';
import { defineComponent } from 'vue';
import store from "@/store";

export default defineComponent({
  name: "component-header",
  setup() {
    let member = store.state.member;
    return {
      selectedKeys1 : ref(['2']),
      member
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


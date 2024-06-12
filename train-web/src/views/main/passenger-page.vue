<template>
  <h1>passenger</h1>

    <div>
      <a-button type="primary" @click="showModal">Open Modal</a-button>
      <a-modal v-model:open="open" title="Basic Modal" @ok="handleOk">
          <a-form :model="passenger" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-form-item label="姓名">
              <a-input v-model:value="passenger.name" />
            </a-form-item>
            <a-form-item label="身份证">
              <a-input v-model:value="passenger.idCard" />
            </a-form-item>
            <a-form-item label="类型">
              <a-select v-model:value="passenger.type">
                <a-select-option value="1" >成人</a-select-option>
                <a-select-option value="1" >儿童</a-select-option>
                <a-select-option value="1" >学生</a-select-option>
              </a-select>
            </a-form-item>
          </a-form>

      </a-modal>
    </div>

</template>

<script>


import {defineComponent, reactive, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "passenger-view",
  setup() {
    const open = ref(false);
    const showModal = () => {
      open.value = true;
    };
    const passenger = reactive({
      id: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      memberId: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const handleOk = () => {
      axios.post("/member/passenger/save", passenger,).then(response => {
        let data = response.data;
        if (data.code === 200) {
          notification.success({ description: '添加成功！' });
          open.value = false;
        } else {
          notification.error({ description: data.message });
        }
      });
    };


    const labelCol = {
      style: {
        width: '150px',
      },
    };
    const wrapperCol = {
      span: 14,
    };
    return {
      open,
      showModal,
      handleOk,
      passenger,
      labelCol,
      wrapperCol,
    };

  },
});
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
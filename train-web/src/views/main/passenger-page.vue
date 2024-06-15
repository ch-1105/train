<template>
  <h1>passenger</h1>

    <div>
      <!--添加p标签与下面表格拉开空隙-->
      <p>
      <a-button type="primary" @click="showModal">Open Modal</a-button>
      </p>
      <a-table :dataSource="passengerList" :columns="columns" :pagination="pagination" @change="handleTableChange"/>

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


import {defineComponent, onMounted, reactive, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "passenger-view",
  setup() {
    const open = ref(false);
    const showModal = () => {
      open.value = true;
    };

    const passengerList = ref([]);

    const columns = [{
      title: '姓名',
      dataIndex: 'name',
      key: 'name',
    },
      {
        title: '身份证',
        dataIndex: 'idCard',
        key: 'idCard',
      },
      {
        title: '旅客类型',
        dataIndex: 'type',
        key: 'type',
      },]

    //分页变量，内容固定
    const pagination = reactive({
      current: 0,
      total: 1,
      pageSize: 2,
      // showSizeChanger: true,
      // showQuickJumper: true,
      // showTotal: (total) => `共 ${total} 条`,
      // onChange:
    });

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

    const queryList = (param) => {
      console.log("queryList -> 查看当前分页查询参数：",param)

      axios.get("/member/passenger/getList",{
        params:{
          pageNum: param.page,
          pageSize: param.size,
        }}).then(response => {
        let data = response.data;
        if (data.code === 200) {
          console.log("打印list : ",data.data.list)
          passengerList.value = data.data.list;
          console.log("打印value : ",passengerList.value)
          pagination.total = data.data.total;
          pagination.current = param.page;
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const handleTableChange = (pagination) => {
      console.log("查看当前分页查询参数：",pagination)
      queryList({
        page: pagination.current,
        size: pagination.pageSize,
      })
    };

    //等页面渲染后，执行查询，添加表格内容
    onMounted(() => {
      queryList({
        page: 1,
        size : pagination.pageSize
      })
    });

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
      passengerList,
      columns,
      pagination,
      handleTableChange,
    };

  },
});
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
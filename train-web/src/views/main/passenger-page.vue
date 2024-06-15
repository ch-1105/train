<template>
  <h1>passenger</h1>

    <div>
      <!--添加p标签与下面表格拉开空隙-->
      <p>
        <a-space>
          <a-button type="primary" @click="queryList()">刷新</a-button>
          <a-button type="primary" @click="onAdd">新增</a-button>
        </a-space>
      </p>
      <a-table
          :dataSource="passengerList"
          :columns="columns"
          :pagination="pagination"
          @change="handleTableChange"
          :loading="loading"
      >
        <template #bodyCell="{column, record}">
          <template v-if="column.dataIndex === 'operation'">
            <a-space>
              <a @click="onEdit(record)">编辑</a>
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  @confirm="handleDelete(record)"
                  ok-text="确认"
                  cancel-text="取消">
                <a style="color: red">删除</a>
              </a-popconfirm>
            </a-space>
         </template>

          <template v-else-if="column.dataIndex === 'type'">
            <span v-for="item in PASSENGER_TYPE" :key="item.key">
              <span v-if="record.type === item.key">
                {{item.value}}
              </span>
            </span>
          </template>

        </template>
      </a-table>

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
                  <a-select-option v-for="item in PASSENGER_TYPE" :key="item.key" :value="item.key" >{{item.value}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-form>
        </a-modal>
    </div>

</template>

<script>


import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "passenger-view",
  setup() {
    const PASSENGER_TYPE = window.PASSENGER_TYPE;

    const open = ref(false);

    const onAdd = () => {
      passenger.value = {}
      open.value = true;
    };

    const onEdit = (record) => {
      passenger.value = window.Tool.copy(record)
      open.value = true;
    };

    const passengerList = ref([]);

    let loading = ref(false)

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
      },
      {
        title: '行为',
        dataIndex: 'operation',
        key: 'operation',
      },]

    //分页变量，内容固定
    const pagination = ref({
      current: 0,
      total: 1,
      pageSize: 2,
      // showSizeChanger: true,
      // showQuickJumper: true,
      // showTotal: (total) => `共 ${total} 条`,
      // onChange:
    });

    const passenger = ref({
      id: undefined,
      name: undefined,
      idCard: undefined,
      type: undefined,
      memberId: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const handleOk = () => {
      axios.post("/member/passenger/save", passenger.value).then(response => {
        let data = response.data;
        if (data.code === 200) {
          notification.success({ description: '添加成功！' });
          open.value = false;
          queryList({
            page: pagination.value.current,
            size : pagination.value.pageSize
          })
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const queryList = (param) => {
      if (param === undefined|| param === null){
        param = {
          page: 1,
          size: pagination.value.pageSize,
        }
      }

      console.log("queryList -> 查看当前分页查询参数：",param)

      //向后端查询前定义为loading=true
      loading.value = true;
      axios.get("/member/passenger/getList",{
        params:{
          pageNum: param.page,
          pageSize: param.size,
        }}).then(response => {
        //结束加载
        loading.value = false;

        let data = response.data;
        if (data.code === 200) {
          console.log("打印list : ",data.data.list)
          passengerList.value = data.data.list;
          console.log("打印value : ",passengerList.value)
          pagination.value.total = data.data.total;
          pagination.value.current = param.page;
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const handleTableChange = (pagination) => {
      console.log("查看当前分页查询参数：",pagination)
      queryList({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      })
    };

    const handleDelete = (record) => {
      axios.delete("/member/passenger/delete/"+record.id,).then(response => {
          let data = response.data;
          if (data.code === 200){
            notification.success({ description: '删除成功！' });
            queryList({
              page: pagination.value.current,
              size : pagination.value.pageSize
            })
          } else {
            notification.error({ description: data.message });
          }
      });
    };

    //等页面渲染后，执行查询，添加表格内容
    onMounted(() => {
      queryList({
        page: 1,
        size : pagination.value.pageSize
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
      onAdd,
      handleOk,
      passenger,
      labelCol,
      wrapperCol,
      passengerList,
      columns,
      pagination,
      handleTableChange,
      queryList,
      loading,
      onEdit,
      handleDelete,
      PASSENGER_TYPE,
    };

  },
});
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
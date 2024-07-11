<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainSeats"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL" :key="item.key">
          <span v-if="item.key === record.col && item.type === record.seatType">
            {{item.value}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE" :key="item.key">
          <span v-if="item.key === record.seatType ">
            {{item.value}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="open" title="座位" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="trainSeat" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="车次编号">
        <train-select v-model:value="trainSeat.trainCode" />
      </a-form-item>
      <a-form-item label="厢序">
        <a-input v-model:value="trainSeat.carriageIndex" />
      </a-form-item>
      <a-form-item label="排号">
        <a-input v-model:value="trainSeat.row" />
      </a-form-item>

      <a-form-item label="列号">
        <a-select v-model:value="trainSeat.col">
          <a-select-option v-for="item in SEAT_COL" :key="item.key" :value="item.key">
            {{item.value}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="trainSeat.seatType">
          <a-select-option v-for="item in SEAT_TYPE" :key="item.key" :value="item.key">
            {{item.value}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="同车厢座序">
        <a-input v-model:value="trainSeat.carriageSeatIndex" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelect from "@/components/train-select.vue";

export default defineComponent({
  name: "train-seat-view",
  components: {TrainSelect},
  setup() {
    const SEAT_COL = window.SEAT_COL;
    const SEAT_TYPE = window.SEAT_TYPE;
    const open = ref(false);
    let trainSeat = ref({
      id: undefined,
      trainCode: undefined,
      carriageIndex: undefined,
      row: undefined,
      col: undefined,
      seatType: undefined,
      carriageSeatIndex: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const trainSeats = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
    {
      title: '车次编号',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
    {
      title: '厢序',
      dataIndex: 'carriageIndex',
      key: 'carriageIndex',
    },
    {
      title: '排号',
      dataIndex: 'row',
      key: 'row',
    },
    {
      title: '列号',
      dataIndex: 'col',
      key: 'col',
    },
    {
      title: '座位类型',
      dataIndex: 'seatType',
      key: 'seatType',
    },
    {
      title: '同车厢座序',
      dataIndex: 'carriageSeatIndex',
      key: 'carriageSeatIndex',
    },
    {
      title: '操作',
      dataIndex: 'operation'
    }
    ];

    const onAdd = () => {
      trainSeat.value = {};
      open.value = true;
    };

    const onEdit = (record) => {
      trainSeat.value = window.Tool.copy(record);
      open.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train-seat/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.code === 200) {
          notification.success({description: "删除成功！"});
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleOk = () => {
      axios.post("/business/admin/train-seat/save", trainSeat.value).then((response) => {
        let data = response.data;
        if (data.code === 200) {
          notification.success({description: "保存成功！"});
          open.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/train-seat/query-list", {
        params: {
          pageNum: param.page,
          pageSize: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.code === 200) {
          trainSeats.value = data.data.list;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = data.data.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (page) => {
      // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      SEAT_COL,
      SEAT_TYPE,
      trainSeat,
      open,
      trainSeats,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete
    };
  },
});
</script>

<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trains"
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
          <a-popconfirm
              title = "生成座位"
              @confirm = "genSeat(record)"
              ok-text="确认" cancel-text="取消"
          >
            <a style="color: cornflowerblue">生成座位</a>
          </a-popconfirm>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in TRAIN_TYPE" :key="item.key">
          <span v-if="item.key === record.type">
            {{item.value}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="open" title="车次" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="train" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="车次编号">
        <a-input v-model:value="train.code" :disabled="!!train.id"/>
      </a-form-item>
      <a-form-item label="车次类型">
        <a-select v-model:value="train.type">
          <a-select-option v-for="item in TRAIN_TYPE" :key="item.key" :value="item.key">
            {{item.value}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="始发站">
        <station-select-view v-model="train.start" />
      </a-form-item>
      <a-form-item label="始发站拼音">
        <a-input v-model:value="train.startPinyin" disabled />
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="train.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
      <a-form-item label="终点站">
        <station-select-view v-model="train.end" />
      </a-form-item>
      <a-form-item label="终点站拼音">
        <a-input v-model:value="train.endPinyin" disabled/>
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="train.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import {pinyin} from "pinyin-pro";
import StationSelectView from "@/components/station-select.vue";

export default defineComponent({
  name: "train-view",
  components: {StationSelectView},
  setup() {
    const TRAIN_TYPE = window.TRAIN_TYPE;
    const open = ref(false);
    let train = ref({
      id: undefined,
      code: undefined,
      type: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const trains = ref([]);
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
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: '车次类型',
      dataIndex: 'type',
      key: 'type',
    },
    {
      title: '始发站',
      dataIndex: 'start',
      key: 'start',
    },
    {
      title: '始发站拼音',
      dataIndex: 'startPinyin',
      key: 'startPinyin',
    },
    {
      title: '出发时间',
      dataIndex: 'startTime',
      key: 'startTime',
    },
    {
      title: '终点站',
      dataIndex: 'end',
      key: 'end',
    },
    {
      title: '终点站拼音',
      dataIndex: 'endPinyin',
      key: 'endPinyin',
    },
    {
      title: '到站时间',
      dataIndex: 'endTime',
      key: 'endTime',
    },
    {
      title: '操作',
      dataIndex: 'operation'
    }
    ];

    const genSeat = (record) => {
      axios.post("/business/admin/train/generator-train-seat/"+record.code).then((response) => {
        const data = response.data;
        if (data.code === 200) {
          notification.success({description: "生成成功！"});
        } else {
          notification.error({description: data.message});
        }
      })
    }
    const onAdd = () => {
      train.value = {};
      open.value = true;
    };

    const onEdit = (record) => {
      train.value = window.Tool.copy(record);
      open.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/train/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/train/save", train.value).then((response) => {
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
      axios.get("/business/admin/train/query-list", {
        params: {
          pageNum: param.page,
          pageSize: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.code === 200) {
          trains.value = data.data.list;
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
      console.log("train-view组件被创建了 : ", window.TRAIN_TYPE);
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    watch(() => train.value.start, ()=>{
      if (Tool.isNotEmpty(train.value.start)) {
        train.value.startPinyin = pinyin(train.value.start, { toneType: 'none'}).replaceAll(" ", "");
      } else {
        train.value.startPinyin = "";
      }
    }, {immediate: true});

    watch(() => train.value.end, ()=>{
      if (Tool.isNotEmpty(train.value.end)) {
        train.value.endPinyin = pinyin(train.value.end, { toneType: 'none'}).replaceAll(" ", "");
      } else {
        train.value.endPinyin = "";
      }
    }, {immediate: true});

    return {
      TRAIN_TYPE,
      train,
      open,
      trains,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      genSeat
    };
  },
});
</script>

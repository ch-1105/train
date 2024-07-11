<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="jobs"
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
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="定时任务" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="job" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="站名">
        <a-input v-model:value="job.name" />
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="job.namePinyin" disabled />
      </a-form-item>
      <a-form-item label="拼音首字母">
        <a-input v-model:value="job.namePy" disabled />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import {pinyin} from "pinyin-pro";

export default defineComponent({
  name: "job",
  setup() {
    const visible = ref(false);
    let job = ref({
      group: undefined,
      name: undefined,
      description: undefined,
      state: undefined,
      cronExpression: undefined,
      nextFireTime: undefined,
      preFireTime: undefined,
    });
    const jobs = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
      {
        title: '定时任务组别',
        dataIndex: 'group',
        key: 'group',
      },
      {
        title: '定时任务名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '描述',
        dataIndex: 'description',
        key: 'description',
      },
      {
        title: 'corn表达式',
        dataIndex: 'cronExpression',
        key: 'cronExpression'
      }
    ];

    const onAdd = () => {
      job.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      job.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/timer/admin/job/delete/" + record.id).then((response) => {
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
      axios.post("/timer/admin/job/save", job.value).then((response) => {
        let data = response.data;
        if (data.code === 200) {
          notification.success({description: "保存成功！"});
          visible.value = false;
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
      axios.get("/timer/admin/job/query", {
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.code === 200) {
          jobs.value = data.data.list;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = data.data.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = () => {
      handleQuery({
      });
    };

    onMounted(() => {
      handleQuery();
    });


    return {
      job,
      visible,
      jobs,
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

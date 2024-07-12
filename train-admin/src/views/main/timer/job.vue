<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="jobs"
           :columns="columns"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a @click="onRun(record)" style="color: green" >运行</a>
          <a @click="onPause(record)" style="color: #000000" v-if="record.state !== 'PAUSE'">暂停</a>
          <a @click="onResume(record)" style="color: sandybrown" v-else >重启</a>
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
  <a-modal v-model:open="open" title="定时任务" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="job" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="分组">
        <a-input v-model:value="job.group" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="job.name" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="job.description" />
      </a-form-item>
      <a-form-item label="表达式">
        <a-input v-model:value="job.cronExpression" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, ref, onMounted, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
export default defineComponent({
  name: "job",
  setup() {
    const open = ref(false);
    const update = ref(false);
    const add = ref(false);
    let job = ref({
      group: undefined,
      name: undefined,
      description: undefined,
      cronExpression: undefined,
    });
    const jobs = ref([]);

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
        title: '状态',
        dataIndex: 'state',
        key: 'state',
      },
      {
        title: 'corn表达式',
        dataIndex: 'cronExpression',
        key: 'cronExpression'
      },
      {
        title: 'nextFireTime',
        dataIndex: 'nextFireTime',
        key: 'nextFireTime'
      },
      {
        title: 'preFireTime',
        dataIndex: 'preFireTime',
        key: 'preFireTime'
      },
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ];

    const onAdd = () => {
      job.value = {};
      open.value = true;
      add.value = true;
      update.value = false;
      job.value.group = "DEFAULT";
      job.value.name = "com.ch.train.timer.job.QuartzJob";
      job.value.cronExpression = "*/9 * * * * ?"
    };

    const onRun = (record) => {
      axios.post("/timer/admin/job/run",{
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.code === 200) {
          notification.success({description: "开启成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const onResume = (record) => {
      axios.post("/timer/admin/job/resume",{
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.code === 200) {
          notification.success({description: "重启成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const onPause = (record) => {
      axios.post("/timer/admin/job/pause",
        {
          name: record.name,
          group: record.group
        }
      ).then((response) => {
        const data = response.data;
        if (data.code === 200) {
          notification.success({description: "暂停成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const onEdit = (record) => {
      job.value = window.Tool.copy(record);
      open.value = true;
      update.value = true;
      add.value = false;
    };

    const onDelete = (record) => {
      axios.post("/timer/admin/job/delete",{
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.code === 200) {
          notification.success({description: "删除成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleOk = () => {
      if (add.value === true) {
        axios.post("/timer/admin/job/add", job.value).then((response) => {
          let data = response.data;
          if (data.code === 200) {
            notification.success({description: "保存成功！"});
            open.value = false;
            add.value = false;
            handleQuery();
          } else {
            notification.error({description: data.message});
          }
        });
      }else if (update.value === true) {
        axios.post("/timer/admin/job/reschedule", job.value).then((response) => {
          let data = response.data;
          if (data.code === 200) {
            notification.success({description: "保存成功！"});
            open.value = false;
            update.value = false;
            handleQuery();
          } else {
            notification.error({description: data.message});
          }
        });
      }
    };

    const handleQuery = () => {
      loading.value = true;
      axios.get("/timer/admin/job/query", {
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.code === 200) {
          jobs.value = data.data;
          console.log(jobs.value);
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
      open,
      jobs,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      onRun,
      onPause,
      onResume,
    };
  },
});
</script>

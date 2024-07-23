<template>
  <a-select v-model:value="trainCode" show-search allow-clear
            :filter-option="filterTrain"
            @change="onChange"
            :style="' width: ' + localWidth">
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :lable = "item.code + item.start + item.end">
      {{item.code}} | {{item.start}} ~ {{item.end}}
    </a-select-option>
  </a-select>
</template>

<script>
import {onMounted, ref, watch ,defineComponent} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

// eslint-disable-next-line vue/no-export-in-script-setup
export default defineComponent({
  name: "train-select-view",
  props: ["modelValue" , "width"],
  emits: ["update:modelValue", "change"],
  setup(props, {emit}) {
    const trainCode = ref();

    const localWidth = ref(props.width);
    if (Tool.isEmpty(localWidth)){
      localWidth.value = "100%"
    }
    // -------  车次下拉框  -------
    const trains = ref([]);
    // 查询所有车次
    const selectTrain = () => {
      let trains_cache = SessionStorage.get(SESSION_TRAINS);
      if (Tool.isNotEmpty(trains_cache)) {
        console.log("从缓存中获取车次数据");
        trains.value = trains_cache;
      } else {
        axios.get("/business/admin/train/query-all-train").then((response) => {
          let data = response.data;
          if (data.code === 200) {
            console.log(data.data)
            trains.value = data.data;
            SessionStorage.set(SESSION_TRAINS, trains.value);
            console.log("从接口获取车次数据");
          } else {
            notification.error({description: data.message});
          }
        });
      }

    };
    //车次下拉框筛选
    const filterTrain = (input, option) => {
      return (
          option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    };

    onMounted (() => {
      selectTrain();
    });
    const onChange = (value) => {
      console.log("update:modelValue", value);
      emit("update:modelValue", value);

      let train = trains.value.filter(item => item.code === value)[0];
      if(Tool.isEmpty(train)){
        train = {}
      }
      emit("change", train);
    };

    //利用watch监听modelValue的变化，将modelValue赋值给trainCode
    watch(() => props.modelValue, ()=>{
      console.log("props.modelValue : " ,props.modelValue)
      trainCode.value = props.modelValue;
    }, {immediate: true});

    return {
      trainCode,
      trains,
      onChange,
      filterTrain,
      localWidth
    };
  },
});
</script>
<template>
  <a-select v-model:value="name" show-search allow-clear
            :filter-option="filterStation"
            @change="onChange"
            :style="' width: ' + localWidth">
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :lable = "item.name + item.namePinyin + item.namePy">
      {{item.name}} | {{item.namePinyin}} ~ {{item.namePy}}
    </a-select-option>
  </a-select>
</template>

<script>
import {onMounted, ref, watch ,defineComponent} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

// eslint-disable-next-line vue/no-export-in-script-setup
export default defineComponent({
  name: "station-select-view",
  props: ["modelValue" , "width"],
  emits: ["update:modelValue", "change"],
  setup(props, {emit}) {
    const name = ref();

    const localWidth = ref(props.width);
    if (Tool.isEmpty(localWidth)){
      localWidth.value = "100%"
    }
    // -------  车次下拉框  -------
    const stations = ref([]);
    // 查询所有车次
    const selectStation = () => {
      axios.get("/business/passenger/station/query-all-station").then((response) => {
        let data = response.data;
        if (data.code === 200) {
          console.log(data.data)
          stations.value = data.data;
        } else {
          notification.error({description: data.message});
        }
      });
    };
    //车次下拉框筛选
    const filterStation = (input, option) => {
      return (
          option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    };

    onMounted (() => {
      selectStation();
    });
    const onChange = (value) => {
      console.log("update:modelValue", value);
      emit("update:modelValue", value);

      let station = stations.value.filter(item => item.code === value)[0];
      if(Tool.isEmpty(station)){
        station = {}
      }
      emit("change", station);
    };

    //利用watch监听modelValue的变化，将modelValue赋值给name
    watch(() => props.modelValue, ()=>{
      console.log("props.modelValue : " ,props.modelValue)
      name.value = props.modelValue;
    }, {immediate: true});

    return {
      name,
      stations,
      onChange,
      filterStation,
      localWidth
    };
  },
});
</script>
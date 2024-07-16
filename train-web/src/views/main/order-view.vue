<template>
  <div class="order-train">
    <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>次&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.start}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.startTime}})</span>&nbsp;
    <span class="order-train-main">——</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.end}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.endTime}})</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{item.value}}</span>：
        <span class="order-train-ticket-main">{{item.price}}￥</span>&nbsp;
        <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;张票&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
    </div>

    <a-divider></a-divider>
    <b>勾选要购票的乘客：</b>&nbsp;
    <div class="passengerList">{{passengerList}}</div>
    <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions" style="width: 100%"/>
    <br/>
    选中乘客 : {{passengerChecks}}
    车票变化 : {{tickets}}

    <div class="order-tickets">
      <a-row class="order-tickets-header" v-if="tickets.length > 0">
        <a-col :span="2">乘客</a-col>
        <a-col :span="6">身份证</a-col>
        <a-col :span="4">票种</a-col>
        <a-col :span="4">座位类型</a-col>
      </a-row>
      <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
        <a-col :span="2">{{ticket.passengerName}}</a-col>
        <a-col :span="6">{{ticket.passengerIdCard}}</a-col>
        <a-col :span="4">
          <a-select v-model:value="ticket.passengerType" style="width: 100%">
            <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.key" :value="item.key">
              {{item.value}}
            </a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select v-model:value="ticket.seatTypeCode" style="width: 100%">
            <a-select-option v-for="item in seatTypes" :key="item.key" :value="item.key">
              {{item.value}}
            </a-select-option>
          </a-select>
        </a-col>
      </a-row>
    </div>

    <div v-if="tickets.length > 0">
      <a-button type="primary" size="large" @click="finishCheckPassenger">提交订单</a-button>
    </div>

    <a-modal v-model:open="open" title="请核对以下信息"
             style="top: 50px; width: 800px"
             ok-text="确认" cancel-text="取消"
             @ok="showFirstImageCodeModal">
      <div class="order-tickets">
        <a-row class="order-tickets-header" v-if="tickets.length > 0">
          <a-col :span="3">乘客</a-col>
          <a-col :span="15">身份证</a-col>
          <a-col :span="3">票种</a-col>
          <a-col :span="3">座位类型</a-col>
        </a-row>
        <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
          <a-col :span="3">{{ticket.passengerName}}</a-col>
          <a-col :span="15">{{ticket.passengerIdCard}}</a-col>
          <a-col :span="3">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.key">
            <span v-if="item.key === ticket.passengerType">
              {{item.value}}
            </span>
          </span>
          </a-col>
          <a-col :span="3">
          <span v-for="item in seatTypes" :key="item.key">
            <span v-if="item.key === ticket.seatTypeCode">
              {{item.value}}
            </span>
          </span>
          </a-col>
        </a-row>
      </div>
    </a-modal>
    
  </div>
</template>

<script>
import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
  export default defineComponent({
    name: "order-view",
    setup() {
      const open = ref(false);
      const passengerList = ref([]);
      const passengerOptions = ref([]);
      const passengerChecks = ref([]);

      const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE;

      const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
      console.log("dailyTrainTicket :  ",dailyTrainTicket);
      const SEAT_TYPE = window.SEAT_TYPE;
      // 本车次提供的座位类型seatTypes，含票价，余票等信息，例：
      // {
      //   type: "YDZ",
      //   key: "1",
      //   value: "一等座",
      //   count: "100",
      //   price: "50",
      // }
      // 关于SEAT_TYPE[KEY]：当知道某个具体的属性xxx时，可以用obj.xxx，当属性名是个变量时，可以使用obj[xxx]
      const seatTypes = [];
      console.log("seatTypes : ",seatTypes)
      for (let KEY in SEAT_TYPE) {
        let key = KEY.toLowerCase().toString();
        if(dailyTrainTicket[key] >= 0){
          // console.log("dailyTrainTicket[key] : ",dailyTrainTicket[key])
          // console.log("KEY : ",KEY)
          // console.log("key[KEY][\"key\"] : ",SEAT_TYPE[KEY]["key"])
          // console.log("SEAT_TYPE[KEY][\"value\"] : ",SEAT_TYPE[KEY]["value"])
          // console.log("dailyTrainTicket[key] : ",dailyTrainTicket[key])
          // console.log("SEAT_TYPE[KEY][\"price\"] : ",SEAT_TYPE[KEY]["price"])
          seatTypes.push({
            type: KEY,
            key: SEAT_TYPE[KEY]["key"],
            value: SEAT_TYPE[KEY]["value"],
            count: dailyTrainTicket[key],
            price: SEAT_TYPE[KEY]["price"],
          })
        }
      }

      // 购票列表，用于界面展示，并传递到后端接口，用来描述：哪个乘客购买什么座位的票
      // {
      //   passengerId: 123,
      //   passengerType: "1",
      //   passengerName: "张三",
      //   passengerIdCard: "12323132132",
      //   seatTypeCode: "1",
      //   seat: "C1"
      // }
      const tickets = ref([]);
      watch(passengerChecks, (oldValue,newValue) => {
        console.log("发生变化 :   ",oldValue,newValue);
        // 每次有变化时，把购票列表清空，重新构造列表
        tickets.value = [];
        passengerChecks.value.forEach(item => {
          tickets.value.push({
            passengerId: item.id,
            passengerType: item.type,
            passengerName: item.name,
            passengerIdCard: item.idCard,
            seatTypeCode: seatTypes[0].key,
          });
        })
      }, {immediate: true});

      const getMinePassengers = () => {
        axios.get("/member/passenger/getMine").then(response => {
          let data = response.data;
          if (data.code === 200){
            passengerList.value = data.data;
            console.log("passengerList.value : ",passengerList.value)
            passengerList.value.forEach(item => {
              passengerOptions.value.push({
                label: item.name,
                value: item,
              })
            })
          } else {
            notification.error({ description: data.message });
          }
        });
      }

      const finishCheckPassenger = () => {
        if (tickets.value.length > 5){
          notification.error({ description: "超出购票限制" });
          return;
        }
        open.value = true;
      }
      const showFirstImageCodeModal = () => {
        open.value = false;
      }

      onMounted(() => {
        getMinePassengers();
      });
      return {
        dailyTrainTicket,
        seatTypes,
        passengerList,
        passengerOptions,
        passengerChecks,
        tickets,
        PASSENGER_TYPE_ARRAY,
        open,
        showFirstImageCodeModal,
        finishCheckPassenger,
      };
    },
  });
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}
.order-train .order-train-ticket {
  margin-top: 15px;
}
.order-train .order-train-ticket .order-train-ticket-main {
  color: red;
  font-size: 18px;
}
</style>
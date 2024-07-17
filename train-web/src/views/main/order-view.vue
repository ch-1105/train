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
             @ok="handleOk">
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

        <br/>
        <div v-if="chooseSeatType === 0" style="color: red;">
          您购买的车票不支持选座
          <div>12306规则：只有全部是一等座或全部是二等座才支持选座</div>
          <div>12306规则：余票小于一定数量时，不允许选座（本项目以20为例）</div>
        </div>
        <div v-else style="text-align: center">
          <a-switch class="choose-seat-item" v-for="item in SEAT_COL_ARRAY" :key="item.key"
                    v-model:checked="chooseSeatObj[item.key + '1']" :checked-children="item.value" :un-checked-children="item.value" />
          <div v-if="tickets.length > 1">
            <a-switch class="choose-seat-item" v-for="item in SEAT_COL_ARRAY" :key="item.key"
                      v-model:checked="chooseSeatObj[item.key + '2']" :checked-children="item.value" :un-checked-children="item.value" />
          </div>
          <div style="color: #999999">提示：您可以选择{{tickets.length}}个座位</div>
        </div>
        <br>
        <a-button type="danger" block @click="handleOk">输入验证码后开始购票</a-button>
      </div>
    </a-modal>
    
  </div>
</template>

<script>
import {computed, defineComponent, onMounted, ref, watch} from 'vue';
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

      // 0：不支持选座；1：选一等座；2：选二等座
      const chooseSeatType = ref(0);
      // 根据选择的座位类型，计算出对应的列，比如要选的是一等座，就筛选出ACDF，要选的是二等座，就筛选出ABCDF
      const SEAT_COL_ARRAY = computed(() => {
        return window.SEAT_COL_ARRAY.filter(item => item.type === chooseSeatType.value);
      });
      // 选择的座位
      // {
      //   A1: false, C1: true，D1: false, F1: false，
      //   A2: false, C2: false，D2: true, F2: false
      // }
      const chooseSeatObj = ref({});
      watch(() => SEAT_COL_ARRAY.value, () => {
        chooseSeatObj.value = {};
        for (let i = 1; i <= 2; i++) {
          SEAT_COL_ARRAY.value.forEach((item) => {
            chooseSeatObj.value[item.key + i] = false;
          })
        }
        console.log("初始化两排座位，都是未选中：", chooseSeatObj.value);
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

        // 校验余票,这里需要使用临时变量
        let copySeatTypes = Tool.copy(seatTypes);
        for (let i = 0; i < tickets.value.length; i++) {
          let ticket = tickets.value[i];
          for (let j = 0; j < copySeatTypes.length; j++) {
            let seatType = copySeatTypes[j];
            // 当前选中车票类型与座位类型匹配，则减掉余票
            if (ticket.seatTypeCode === seatType.key){
              seatType.count--;
              if (seatType.count < 0){
                notification.error({ description: "当前座位类型余票不足" });
                return;
              }
            }
          }
        }
        notification.success({ description: "校验通过" });

        // 判断是否支持选座，只有纯一等座和纯二等座支持选座
        // 先筛选出购票列表中的所有座位类型，比如四张表：[1, 1, 2, 2]
        let ticketSeatTypeCodes = [];
        for (let i = 0; i < tickets.value.length; i++) {
          let ticket = tickets.value[i];
          ticketSeatTypeCodes.push(ticket.seatTypeCode);
        }
        // 为购票列表中的所有座位类型去重：[1, 2]
        const ticketSeatTypeCodesSet = Array.from(new Set(ticketSeatTypeCodes));
        console.log("选好的座位类型：", ticketSeatTypeCodesSet);
        if (ticketSeatTypeCodesSet.length !== 1) {
          console.log("选了多种座位，不支持选座");
          chooseSeatType.value = 0;
        } else {
          // ticketSeatTypeCodesSet.length === 1，即只选择了一种座位（不是一个座位，是一种座位）
          if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.YDZ.key) {
            console.log("一等座选座");
            chooseSeatType.value = SEAT_TYPE.YDZ.key;
          } else if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.EDZ.key) {
            console.log("二等座选座");
            chooseSeatType.value = SEAT_TYPE.EDZ.key;
          } else {
            console.log("不是一等座或二等座，不支持选座");
            chooseSeatType.value = 0;
          }
        }

        // 余票小于20张时，不允许选座，否则选座成功率不高，影响出票
        if (chooseSeatType.value !== 0) {
          for (let i = 0; i < seatTypes.length; i++) {
            let seatType = seatTypes[i];
            // 找到同类型座位
            if (ticketSeatTypeCodesSet[0] === seatType.code) {
              // 判断余票，小于20张就不支持选座
              if (seatType.count < 20) {
                console.log("余票小于20张就不支持选座")
                chooseSeatType.value = 0;
                break;
              }
            }
          }
        }

        open.value = true;
      }

      const handleOk = () => {
        console.log("选好的座位：", chooseSeatObj.value);

        // 设置每张票的座位
        // 先清空购票列表的座位，有可能之前选了并设置座位了，但选座数不对被拦截了，又重新选一遍
        for (let i = 0; i < tickets.value.length; i++) {
          tickets.value[i].seat = null;
        }
        let i = -1;
        // 要么不选座位，要么所选座位应该等于购票数，即i === (tickets.value.length - 1)
        for (let key in chooseSeatObj.value) {
          if (chooseSeatObj.value[key]) {
            i++;
            if (i > tickets.value.length - 1) {
              notification.error({description: '所选座位数大于购票数'});
              return;
            }
            tickets.value[i].seat = key;
          }
        }
        if (i > -1 && i < (tickets.value.length - 1)) {
          notification.error({description: '所选座位数小于购票数'});
          return;
        }

        console.log("最终购票：", tickets.value);

        axios.post("/business/passenger/confirm-order/save", {
          dailyTrainTicketId: dailyTrainTicket.id,
          tickets: tickets.value,
          date: dailyTrainTicket.date,
          end: dailyTrainTicket.end,
          start: dailyTrainTicket.start,
          trainCode: dailyTrainTicket.trainCode,

        }).then((response) => {
          let data = response.data;
          if (data.code === 200) {
            notification.success({description: data.message});
          } else {
            notification.error({description: data.message});
          }
        });
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
        chooseSeatObj,
        SEAT_COL_ARRAY,
        chooseSeatType,
        handleOk,
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

.order-tickets {
  margin: 10px 0;
}
.order-tickets .ant-col {
  padding: 5px 10px;
}
.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: white;
  font-size: 16px;
  padding: 5px 0;
}
.order-tickets .order-tickets-row {
  border: solid 1px cornflowerblue;
  border-top: none;
  vertical-align: middle;
  line-height: 30px;
}

.order-tickets .choose-seat-item {
  margin: 5px 5px;
}
</style>
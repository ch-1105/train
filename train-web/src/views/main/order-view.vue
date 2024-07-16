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
  </div>
</template>

<script>
import { defineComponent } from 'vue';
  export default defineComponent({
    name: "order-view",
    setup() {
      const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
      console.log("dailyTrainTicket :  ",dailyTrainTicket);
      const SEAT_TYPE = window.SEAT_TYPE;
      console.log(SEAT_TYPE)
      // 本车次提供的座位类型seatTypes，含票价，余票等信息，例：
      // {
      //   type: "YDZ",
      //   code: "1",
      //   desc: "一等座",
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
            code: SEAT_TYPE[KEY]["key"],
            value: SEAT_TYPE[KEY]["value"],
            count: dailyTrainTicket[key],
            price: SEAT_TYPE[KEY]["price"],
          })
        }
      }
      return {
        dailyTrainTicket,
        seatTypes,
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
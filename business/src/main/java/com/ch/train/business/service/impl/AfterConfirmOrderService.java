package com.ch.train.business.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import com.ch.train.business.domain.ConfirmOrder;
import com.ch.train.business.domain.DailyTrainSeat;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.enums.ConfirmOrderStatusEnum;
import com.ch.train.business.feign.MemberFeign;
import com.ch.train.business.mapper.ConfirmOrderMapper;
import com.ch.train.business.mapper.CustomizedDailyTrainTicketMapper;
import com.ch.train.business.mapper.DailyTrainSeatMapper;
import com.ch.train.business.request.ConfirmOrderTicketRequest;
import com.ch.train.business.service.ConfirmOrderService;
import com.ch.train.business.service.DailyTrainTicketService;
import com.ch.train.common.request.MemberTicketRequest;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AfterConfirmOrderService{

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;
    @Resource
    private CustomizedDailyTrainTicketMapper customizedDailyTrainTicketMapper;
    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    @Resource
    private MemberFeign memberFeign;

    // 选中后事务
        // 座位表更新数量
        // 余票表更新状态
        // 订单表更新状态
    @Transactional
    public void doConfirmOrder(DailyTrainTicket ticket,
                               List<DailyTrainSeat> systemChooseedList,
                               List<ConfirmOrderTicketRequest> tickets,
                               ConfirmOrder confirmOrder
    ) {
        int j = 0 ;
        DateTime now = DateTime.now();
        for (DailyTrainSeat dailyTrainSeat : systemChooseedList) {
            Long id = dailyTrainSeat.getId();
            String sell = dailyTrainSeat.getSell();
            DailyTrainSeat saveSeat = new DailyTrainSeat();
            saveSeat.setId(id);
            saveSeat.setSell(sell);
            saveSeat.setUpdateTime(DateTime.now());
            dailyTrainSeatMapper.updateById(saveSeat);

            // 计算受影响区间
            // 计算这个站卖出去后，影响了哪些站的余票库存
            // 参照2-3节 如何保证不超卖、不少卖，还要能承受极高的并发 10:30左右
            // 影响的库存：本次选座之前没卖过票的，和本次购买的区间有交集的区间
            // 假设10个站，本次买4~7站
            // 原售：001000001
            // 购买：000011100
            // 新售：001011101
            // 影响：XXX11111X
            // Integer startIndex = 4;
            // Integer endIndex = 7;
            // Integer minStartIndex = startIndex - 往前碰到的最后一个0;
            // Integer maxStartIndex = endIndex - 1;
            // Integer minEndIndex = startIndex + 1;
            // Integer maxEndIndex = endIndex + 往后碰到的最后一个0;
            int start = ticket.getStartIndex();
            int end = ticket.getEndIndex();
            char[] charArray = saveSeat.getSell().toCharArray();
            int maxStartIndex = end - 1; //影响范围 min开始 - max开始,这里max限制为end-1
            int minEndIndex = start+1;  //影响范围 min结束 - max结束,这里min限制为start+1
            int minStartIndex = 0;
            int maxEndIndex = charArray.length;
            // 计算minStartIndex,往前的第一个1
            for (int i = start; i >= 0 ; i--) {
                if (charArray[i] == '1') {
                    minStartIndex = i;
                    break;
                }
            }
            // 计算maxEndIndex,往后的第一个1
            for (int i = end; i < charArray.length ; i++) {
                if (charArray[i] == '1') {
                    maxEndIndex = i;
                    break;
                }
            }
            log.info("当前初始区间 : 起始站:{}, 终点站:{}", maxStartIndex, minEndIndex);
            log.info("当前计算区间 : 起始站:{}, 终点站:{}", minStartIndex, maxEndIndex);

            customizedDailyTrainTicketMapper.upCountBySell(
                    ticket.getDate(),
                    ticket.getTrainCode(),
                    dailyTrainSeat.getSeatType(),
                    minStartIndex,
                    maxStartIndex,
                    minEndIndex,
                    maxEndIndex);
            // 更新余票状态
            MemberTicketRequest memberTicketRequest = new MemberTicketRequest();
            memberTicketRequest.setId(IdUtil.getSnowflakeNextId());
            memberTicketRequest.setMemberId(confirmOrder.getMemberId());
            memberTicketRequest.setPassengerId(tickets.get(j).getPassengerId());
            memberTicketRequest.setPassengerName(tickets.get(j).getPassengerName());
            memberTicketRequest.setTrainDate(ticket.getDate());
            memberTicketRequest.setTrainCode(ticket.getTrainCode());
            memberTicketRequest.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
            memberTicketRequest.setSeatRow(dailyTrainSeat.getRow());
            memberTicketRequest.setSeatCol(dailyTrainSeat.getCol());
            memberTicketRequest.setStartStation(ticket.getStart());
            memberTicketRequest.setStartTime(ticket.getStartTime());
            memberTicketRequest.setEndStation(ticket.getEnd());
            memberTicketRequest.setEndTime(ticket.getEndTime());
            memberTicketRequest.setSeatType(dailyTrainSeat.getSeatType());
            memberTicketRequest.setCreateTime(now);
            memberTicketRequest.setUpdateTime(now);
            memberFeign.save(memberTicketRequest);
            j++;

            confirmOrder.setStatus(ConfirmOrderStatusEnum.SUCCESS.getCode());
            confirmOrderMapper.updateById(confirmOrder);
        }
    }
}

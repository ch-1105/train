package com.ch.train.business.service.impl;

import cn.hutool.core.date.DateTime;
import com.ch.train.business.domain.DailyTrainSeat;
import com.ch.train.business.mapper.ConfirmOrderMapper;
import com.ch.train.business.mapper.DailyTrainSeatMapper;
import com.ch.train.business.service.ConfirmOrderService;
import com.ch.train.business.service.DailyTrainCarriageService;
import com.ch.train.business.service.DailyTrainTicketService;
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
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    // 选中后事务
        // 座位表更新数量
        // 余票表更新状态
        // 订单表更新状态
    @Transactional
    public void doConfirmOrder(List<DailyTrainSeat> systemChooseedList) {
        for (DailyTrainSeat dailyTrainSeat : systemChooseedList) {
            Long id = dailyTrainSeat.getId();
            String sell = dailyTrainSeat.getSell();
            DailyTrainSeat saveSeat = new DailyTrainSeat();
            saveSeat.setId(id);
            saveSeat.setSell(sell);
            saveSeat.setUpdateTime(DateTime.now());
            dailyTrainSeatMapper.updateById(saveSeat);
        }
    }
}

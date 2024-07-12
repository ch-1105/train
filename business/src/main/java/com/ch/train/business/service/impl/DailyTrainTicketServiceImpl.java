package com.ch.train.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.service.DailyTrainTicketService;
import com.ch.train.business.mapper.DailyTrainTicketMapper;
import org.springframework.stereotype.Service;

/**
* @author ch051
* @description 针对表【daily_train_ticket(余票信息)】的数据库操作Service实现
* @createDate 2024-07-12 08:58:05
*/
@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket>
    implements DailyTrainTicketService{

}





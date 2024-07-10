package com.ch.train.business.mapper;

import com.ch.train.business.domain.TrainSeat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author ch051
* @description 针对表【train_seat(座位)】的数据库操作Mapper
* @createDate 2024-06-30 19:02:17
* @Entity com.ch.train.business.domain.TrainSeat
*/
public interface TrainSeatMapper extends BaseMapper<TrainSeat> {
    void deleteByTrainCode(String trainCode);
}





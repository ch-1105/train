package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.DailyTrainSeat;
import com.ch.train.business.request.DailyTrainSeatQueryRequest;
import com.ch.train.business.request.DailyTrainSeatSaveRequest;
import com.ch.train.business.response.DailyTrainSeatQueryResponse;

import com.ch.train.common.response.PageResponse;

import java.util.Date;
import java.util.List;

public interface DailyTrainSeatService extends IService<DailyTrainSeat>{

    void save(DailyTrainSeatSaveRequest request);

    PageResponse<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryRequest request);

    void delete(Long id);

    void generateDailyTrainCode(Date date, String trainCode);

    Integer countSeatBySeatType(Date date, String trainCode, String seatType);

    List<DailyTrainSeat> getByTrainCarriageIndex(String trainCode, Date date,
                                                 Integer carriageIndex);
}
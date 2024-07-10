package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.TrainSeat;
import com.ch.train.business.request.TrainSeatQueryRequest;
import com.ch.train.business.request.TrainSeatSaveRequest;
import com.ch.train.business.response.TrainSeatQueryResponse;

import com.ch.train.common.response.PageResponse;

public interface TrainSeatService extends IService<TrainSeat>{

    void save(TrainSeatSaveRequest request);

    PageResponse<TrainSeatQueryResponse> queryList(TrainSeatQueryRequest request);

    void delete(Long id);

    void generatorTrainSeat( String trainCode);
}
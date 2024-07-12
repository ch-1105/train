package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.TrainStation;
import com.ch.train.business.request.TrainStationQueryRequest;
import com.ch.train.business.request.TrainStationSaveRequest;
import com.ch.train.business.response.TrainStationQueryResponse;

import com.ch.train.common.response.PageResponse;

import java.util.List;

public interface TrainStationService extends IService<TrainStation>{

    void save(TrainStationSaveRequest request);

    PageResponse<TrainStationQueryResponse> queryList(TrainStationQueryRequest request);

    void delete(Long id);

    List<TrainStation> getTrainStations(String trainCode);
}
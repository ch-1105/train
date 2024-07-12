package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.DailyTrainStation;
import com.ch.train.business.request.DailyTrainStationQueryRequest;
import com.ch.train.business.request.DailyTrainStationSaveRequest;
import com.ch.train.business.response.DailyTrainStationQueryResponse;

import com.ch.train.common.response.PageResponse;

public interface DailyTrainStationService extends IService<DailyTrainStation>{

    void save(DailyTrainStationSaveRequest request);

    PageResponse<DailyTrainStationQueryResponse> queryList(DailyTrainStationQueryRequest request);

    void delete(Long id);
}
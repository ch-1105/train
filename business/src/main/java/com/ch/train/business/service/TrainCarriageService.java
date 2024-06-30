package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.TrainCarriage;
import com.ch.train.business.request.TrainCarriageQueryRequest;
import com.ch.train.business.request.TrainCarriageSaveRequest;
import com.ch.train.business.response.TrainCarriageQueryResponse;

import com.ch.train.common.response.PageResponse;

public interface TrainCarriageService extends IService<TrainCarriage>{

    void save(TrainCarriageSaveRequest request);

    PageResponse<TrainCarriageQueryResponse> queryList(TrainCarriageQueryRequest request);

    void delete(Long id);
}
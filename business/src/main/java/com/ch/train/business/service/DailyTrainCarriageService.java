package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.DailyTrainCarriage;
import com.ch.train.business.request.DailyTrainCarriageQueryRequest;
import com.ch.train.business.request.DailyTrainCarriageSaveRequest;
import com.ch.train.business.response.DailyTrainCarriageQueryResponse;

import com.ch.train.common.response.PageResponse;

public interface DailyTrainCarriageService extends IService<DailyTrainCarriage>{

    void save(DailyTrainCarriageSaveRequest request);

    PageResponse<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryRequest request);

    void delete(Long id);
}
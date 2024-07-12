package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.DailyTrain;
import com.ch.train.business.request.DailyTrainQueryRequest;
import com.ch.train.business.request.DailyTrainSaveRequest;
import com.ch.train.business.response.DailyTrainQueryResponse;

import com.ch.train.common.response.PageResponse;

import java.util.Date;

public interface DailyTrainService extends IService<DailyTrain>{

    void save(DailyTrainSaveRequest request);

    PageResponse<DailyTrainQueryResponse> queryList(DailyTrainQueryRequest request);

    void delete(Long id);

    void generateBatchDailyTrain(Date date);
}
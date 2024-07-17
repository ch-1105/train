package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.DailyTrainCarriage;
import com.ch.train.business.request.DailyTrainCarriageQueryRequest;
import com.ch.train.business.request.DailyTrainCarriageSaveRequest;
import com.ch.train.business.response.DailyTrainCarriageQueryResponse;

import com.ch.train.common.response.PageResponse;

import java.util.Date;
import java.util.List;

public interface DailyTrainCarriageService extends IService<DailyTrainCarriage>{

    void save(DailyTrainCarriageSaveRequest request);

    PageResponse<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryRequest request);

    void delete(Long id);

    List<DailyTrainCarriage> getByTrainType(String trainCode, Date date, String seatType);
}
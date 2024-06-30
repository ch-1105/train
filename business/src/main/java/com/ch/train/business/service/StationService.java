package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.Station;
import com.ch.train.business.request.StationQueryRequest;
import com.ch.train.business.request.StationSaveRequest;
import com.ch.train.business.response.StationQueryResponse;
import com.ch.train.common.response.PageResponse;

public interface StationService extends IService<Station>{

    void save(StationSaveRequest request);

    PageResponse<StationQueryResponse> queryList(StationQueryRequest request);

    void delete(Long id);
}
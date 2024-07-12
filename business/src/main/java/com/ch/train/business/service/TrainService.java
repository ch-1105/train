package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.Train;
import com.ch.train.business.request.TrainQueryRequest;
import com.ch.train.business.request.TrainSaveRequest;
import com.ch.train.business.response.TrainQueryResponse;

import com.ch.train.common.response.PageResponse;

import java.util.List;

public interface TrainService extends IService<Train>{

    void save(TrainSaveRequest request);

    PageResponse<TrainQueryResponse> queryList(TrainQueryRequest request);

    void delete(Long id);

    List<TrainQueryResponse> queryAllTrain();

    List<Train> getAllTrain() ;
}
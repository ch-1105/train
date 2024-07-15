package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.request.DailyTrainTicketQueryRequest;
import com.ch.train.business.request.DailyTrainTicketSaveRequest;
import com.ch.train.business.response.DailyTrainTicketQueryResponse;

import com.ch.train.common.response.PageResponse;

import java.util.Date;

public interface DailyTrainTicketService extends IService<DailyTrainTicket>{

    void save(DailyTrainTicketSaveRequest request);

    PageResponse<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryRequest request);

    void delete(Long id);

    void generateDailyByTrainCode(Date date, String trainCode);
}
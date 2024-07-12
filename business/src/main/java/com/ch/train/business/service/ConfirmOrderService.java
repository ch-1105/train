package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.ConfirmOrder;
import com.ch.train.business.request.ConfirmOrderQueryRequest;
import com.ch.train.business.request.ConfirmOrderSaveRequest;
import com.ch.train.business.response.ConfirmOrderQueryResponse;

import com.ch.train.common.response.PageResponse;

public interface ConfirmOrderService extends IService<ConfirmOrder>{

    void save(ConfirmOrderSaveRequest request);

    PageResponse<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryRequest request);

    void delete(Long id);
}
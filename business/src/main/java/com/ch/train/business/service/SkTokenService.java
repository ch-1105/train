package com.ch.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.business.domain.SkToken;
import com.ch.train.business.request.SkTokenQueryRequest;
import com.ch.train.business.request.SkTokenSaveRequest;
import com.ch.train.business.response.SkTokenQueryResponse;

import com.ch.train.common.response.PageResponse;

public interface SkTokenService extends IService<SkToken>{

    void save(SkTokenSaveRequest request);

    PageResponse<SkTokenQueryResponse> queryList(SkTokenQueryRequest request);

    void delete(Long id);
}
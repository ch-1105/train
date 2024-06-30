package com.ch.train.${module}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.train.${module}.domain.${Domain};
import com.ch.train.${module}.request.${Domain}QueryRequest;
import com.ch.train.${module}.request.${Domain}SaveRequest;
import com.ch.train.${module}.response.${Domain}QueryResponse;

import com.ch.train.common.response.PageResponse;

public interface ${Domain}Service extends IService<${Domain}>{

    void save(${Domain}SaveRequest request);

    PageResponse<${Domain}QueryResponse> queryList(${Domain}QueryRequest request);

    void delete(Long id);
}
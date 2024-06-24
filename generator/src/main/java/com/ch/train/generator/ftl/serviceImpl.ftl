package com.ch.train.${module}.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.${module}.domain.${Domain};
import com.ch.train.${module}.mapper.${Domain}Mapper;
import com.ch.train.${module}.service.${Domain}Service;
import com.ch.train.${module}.request.${Domain}QueryRequest;
import com.ch.train.${module}.request.${Domain}SaveRequest;
import com.ch.train.${module}.response.${Domain}QueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class ${Domain}ServiceImpl extends ServiceImpl<${Domain}Mapper, ${Domain}> implements ${Domain}Service {

    private static final Logger LOG = LoggerFactory.getLogger(${Domain}Service.class);

    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}SaveRequest request) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(request, ${Domain}.class);
        if (ObjectUtil.isNull(${domain}.getId())) {
            ${domain}.setId(IdUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
        } else {
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateById(${domain});
        }
    }

    public PageResponse<${Domain}QueryResponse> queryList(${Domain}QueryRequest request) {
        QueryWrapper<${Domain}> ${domain}Wrapper = new QueryWrapper<>();
        ${domain}Wrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<${Domain}> ${domain}List = ${domain}Mapper.selectList(${domain}Wrapper);

        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}List);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<${Domain}QueryResponse> list = BeanUtil.copyToList(${domain}List, ${Domain}QueryResponse.class);

        PageResponse<${Domain}QueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }

    public void delete(Long id) {
        ${domain}Mapper.deleteById(id);
    }
}

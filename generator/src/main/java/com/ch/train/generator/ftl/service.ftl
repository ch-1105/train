package com.ch.train.${module}.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.util.SnowUtil;
import com.ch.train.${module}.domain.${Domain};
import com.ch.train.${module}.domain.${Domain}Example;
import com.ch.train.${module}.mapper.${Domain}Mapper;
import com.ch.train.${module}.request.${Domain}QueryRequestuest;
import com.ch.train.${module}.request.${Domain}SaveRequestuest;
import com.ch.train.${module}.response.${Domain}QueryResponseonseonse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${Domain}Service {

    private static final Logger LOG = LoggerFactory.getLogger(${Domain}Service.class);

    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    public void save(${Domain}SaveRequestuest request) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(request, ${Domain}.class);
        if (ObjectUtil.isNull(${domain}.getId())) {
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.insert(${domain});
        } else {
            ${domain}.setUpdateTime(now);
            ${domain}Mapper.updateByPrimaryKey(${domain});
        }
    }

    public PageResponse<${Domain}QueryResponseonse> queryList(${Domain}QueryRequestuest request) {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();

        LOG.info("查询页码：{}", request.getPage());
        LOG.info("每页条数：{}", request.getSize());
        PageHelper.startPage(request.getPage(), request.getSize());
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);

        PageInfo<${Domain}> pageInfo = new PageInfo<>(${domain}List);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<${Domain}QueryResponseonse> list = BeanUtil.copyToList(${domain}List, ${Domain}QueryResponseonse.class);

        PageResponse<${Domain}QueryResponseonse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }

    public void delete(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}

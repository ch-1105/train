package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.SkToken;
import com.ch.train.business.mapper.SkTokenMapper;
import com.ch.train.business.service.SkTokenService;
import com.ch.train.business.request.SkTokenQueryRequest;
import com.ch.train.business.request.SkTokenSaveRequest;
import com.ch.train.business.response.SkTokenQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class SkTokenServiceImpl extends ServiceImpl<SkTokenMapper, SkToken> implements SkTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(SkTokenService.class);

    @Resource
    private SkTokenMapper skTokenMapper;

    @Override
    public void save(SkTokenSaveRequest request) {
        DateTime now = DateTime.now();
        SkToken skToken = BeanUtil.copyProperties(request, SkToken.class);
        if (ObjectUtil.isNull(skToken.getId())) {
            skToken.setId(IdUtil.getSnowflakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            skTokenMapper.insert(skToken);
        } else {
            skToken.setUpdateTime(now);
            skTokenMapper.updateById(skToken);
        }
    }
    @Override
    public PageResponse<SkTokenQueryResponse> queryList(SkTokenQueryRequest request) {
        QueryWrapper<SkToken> skTokenWrapper = new QueryWrapper<>();
        skTokenWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<SkToken> skTokenList = skTokenMapper.selectList(skTokenWrapper);

        PageInfo<SkToken> pageInfo = new PageInfo<>(skTokenList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<SkTokenQueryResponse> list = BeanUtil.copyToList(skTokenList, SkTokenQueryResponse.class);

        PageResponse<SkTokenQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        skTokenMapper.deleteById(id);
    }
}

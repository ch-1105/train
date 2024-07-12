package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.DailyTrain;
import com.ch.train.business.mapper.DailyTrainMapper;
import com.ch.train.business.service.DailyTrainService;
import com.ch.train.business.request.DailyTrainQueryRequest;
import com.ch.train.business.request.DailyTrainSaveRequest;
import com.ch.train.business.response.DailyTrainQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class DailyTrainServiceImpl extends ServiceImpl<DailyTrainMapper, DailyTrain> implements DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Override
    public void save(DailyTrainSaveRequest request) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(request, DailyTrain.class);
        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(IdUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateById(dailyTrain);
        }
    }
    @Override
    public PageResponse<DailyTrainQueryResponse> queryList(DailyTrainQueryRequest request) {
        QueryWrapper<DailyTrain> dailyTrainWrapper = new QueryWrapper<>();
        dailyTrainWrapper.orderByDesc("date");
        dailyTrainWrapper.orderByAsc("code");

        // 查询条件 按日期 或 车次查询
        if (ObjectUtil.isNotNull(request.getDate())) {
            dailyTrainWrapper.eq("date", request.getDate());
        }
        if (ObjectUtil.isNotEmpty(request.getCode())) {
            dailyTrainWrapper.eq("code", request.getCode());
        }

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectList(dailyTrainWrapper);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainQueryResponse> list = BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResponse.class);

        PageResponse<DailyTrainQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        dailyTrainMapper.deleteById(id);
    }
}

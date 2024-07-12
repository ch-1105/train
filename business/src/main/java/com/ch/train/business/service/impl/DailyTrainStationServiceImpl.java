package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.DailyTrainStation;
import com.ch.train.business.mapper.DailyTrainStationMapper;
import com.ch.train.business.service.DailyTrainStationService;
import com.ch.train.business.request.DailyTrainStationQueryRequest;
import com.ch.train.business.request.DailyTrainStationSaveRequest;
import com.ch.train.business.response.DailyTrainStationQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class DailyTrainStationServiceImpl extends ServiceImpl<DailyTrainStationMapper, DailyTrainStation> implements DailyTrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationService.class);

    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;

    @Override
    public void save(DailyTrainStationSaveRequest request) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(request, DailyTrainStation.class);
        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            dailyTrainStation.setId(IdUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.updateById(dailyTrainStation);
        }
    }
    @Override
    public PageResponse<DailyTrainStationQueryResponse> queryList(DailyTrainStationQueryRequest request) {
        QueryWrapper<DailyTrainStation> dailyTrainStationWrapper = new QueryWrapper<>();
        dailyTrainStationWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainStation> dailyTrainStationList = dailyTrainStationMapper.selectList(dailyTrainStationWrapper);

        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainStationQueryResponse> list = BeanUtil.copyToList(dailyTrainStationList, DailyTrainStationQueryResponse.class);

        PageResponse<DailyTrainStationQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        dailyTrainStationMapper.deleteById(id);
    }
}

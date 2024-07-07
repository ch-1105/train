package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.Station;
import com.ch.train.business.mapper.StationMapper;
import com.ch.train.business.service.StationService;
import com.ch.train.business.request.StationQueryRequest;
import com.ch.train.business.request.StationSaveRequest;
import com.ch.train.business.response.StationQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    private static final Logger LOG = LoggerFactory.getLogger(StationService.class);

    @Resource
    private StationMapper stationMapper;

    public void save(StationSaveRequest request) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(request, Station.class);
        if (ObjectUtil.isNull(station.getId())) {
            station.setId(IdUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            stationMapper.insert(station);
        } else {
            station.setUpdateTime(now);
            stationMapper.updateById(station);
        }
    }

    public PageResponse<StationQueryResponse> queryList(StationQueryRequest request) {
        QueryWrapper<Station> stationWrapper = new QueryWrapper<>();
        stationWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Station> stationList = stationMapper.selectList(stationWrapper);

        PageInfo<Station> pageInfo = new PageInfo<>(stationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<StationQueryResponse> list = BeanUtil.copyToList(stationList, StationQueryResponse.class);

        PageResponse<StationQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }

    public void delete(Long id) {
        stationMapper.deleteById(id);
    }

    @Override
    public List<StationQueryResponse> queryAll() {
        QueryWrapper<Station> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name_pinyin");
        List<Station> stations = stationMapper.selectList(wrapper);
        return BeanUtil.copyToList(stations, StationQueryResponse.class);
    }
}

package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ch.train.business.domain.Station;
import com.ch.train.common.utils.GlobalException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.TrainStation;
import com.ch.train.business.mapper.TrainStationMapper;
import com.ch.train.business.service.TrainStationService;
import com.ch.train.business.request.TrainStationQueryRequest;
import com.ch.train.business.request.TrainStationSaveRequest;
import com.ch.train.business.response.TrainStationQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class TrainStationServiceImpl extends ServiceImpl<TrainStationMapper, TrainStation> implements TrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);

    @Resource
    private TrainStationMapper trainStationMapper;

    public void save(TrainStationSaveRequest request) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(request, TrainStation.class);
        if (ObjectUtil.isNull(trainStation.getId())) {
            TrainStation unionTrainStation = getUnionTrainStation(request.getTrainCode(), request.getName());
            if (ObjectUtil.isNotNull(unionTrainStation)) {
                throw new GlobalException(1002, "该火车站点及站名已存在");
            }
            unionTrainStation = getUnionTrainStation(request.getTrainCode(), request.getStationIndex());
            if (ObjectUtil.isNotNull(unionTrainStation)) {
                throw new GlobalException(1002, "该火车站点及站序已存在");
            }
            trainStation.setId(IdUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateById(trainStation);
        }
    }

    public PageResponse<TrainStationQueryResponse> queryList(TrainStationQueryRequest request) {
        QueryWrapper<TrainStation> trainStationWrapper = new QueryWrapper<>();
        trainStationWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<TrainStation> trainStationList = trainStationMapper.selectList(trainStationWrapper);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainStationQueryResponse> list = BeanUtil.copyToList(trainStationList, TrainStationQueryResponse.class);

        PageResponse<TrainStationQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }

    public void delete(Long id) {
        trainStationMapper.deleteById(id);
    }

    private TrainStation getUnionTrainStation(String trainCode,String name) {
        QueryWrapper<TrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("train_code", trainCode);
        queryWrapper.eq("name", name);
        List<TrainStation> list = trainStationMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private TrainStation getUnionTrainStation(String name,Integer stationIndex) {
        QueryWrapper<TrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("station_index", stationIndex);
        List<TrainStation> list = trainStationMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}

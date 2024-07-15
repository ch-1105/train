package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.DailyTrainStation;
import com.ch.train.business.domain.TrainStation;
import com.ch.train.business.mapper.DailyTrainStationMapper;
import com.ch.train.business.request.DailyTrainStationQueryRequest;
import com.ch.train.business.request.DailyTrainStationSaveRequest;
import com.ch.train.business.response.DailyTrainStationQueryResponse;
import com.ch.train.business.service.DailyTrainStationService;
import com.ch.train.business.service.TrainStationService;
import com.ch.train.common.response.PageResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainStationServiceImpl extends ServiceImpl<DailyTrainStationMapper, DailyTrainStation> implements DailyTrainStationService {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainStationService.class);

    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;

    @Resource
    private TrainStationService trainStationService;

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

        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainStation> dailyTrainStationList = dailyTrainStationMapper.selectList(dailyTrainStationWrapper);

        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStationList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

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

    @Override
    @Transactional
    public void generateDailyTrainCode(Date date, String trainCode) {
        log.info("生成日期【{}】车次【{}】的车站信息开始", DateUtil.formatDate(date), trainCode);

        DateTime now = DateTime.now();
        //先删除车次
        QueryWrapper<DailyTrainStation> wrapper = new QueryWrapper<>();
        wrapper.eq("date", date);
        wrapper.eq("train_code", trainCode);
        dailyTrainStationMapper.delete(wrapper);

        //生成车次
        List<TrainStation> trainStations = trainStationService.getTrainStations(trainCode);
        if (CollUtil.isEmpty(trainStations)) {
            log.info("没有车站数据，请先导入车站数据");
            return;
        }
        for (TrainStation trainStation : trainStations) {
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(IdUtil.getSnowflakeNextId());
            dailyTrainStation.setDate(date);
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setTrainCode(trainCode);
            dailyTrainStationMapper.insert(dailyTrainStation);
        }
        log.info("生成日期【{}】车次【{}】的车站信息结束", DateUtil.formatDate(date), trainCode);
    }
}

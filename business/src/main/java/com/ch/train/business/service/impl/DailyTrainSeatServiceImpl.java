package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.DailyTrainSeat;
import com.ch.train.business.domain.TrainSeat;
import com.ch.train.business.domain.TrainStation;
import com.ch.train.business.mapper.DailyTrainSeatMapper;
import com.ch.train.business.request.DailyTrainSeatQueryRequest;
import com.ch.train.business.request.DailyTrainSeatSaveRequest;
import com.ch.train.business.response.DailyTrainSeatQueryResponse;
import com.ch.train.business.service.DailyTrainSeatService;
import com.ch.train.business.service.TrainSeatService;
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
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat> implements DailyTrainSeatService {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainSeatService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private TrainStationService trainStationService;

    @Override
    public void save(DailyTrainSeatSaveRequest request) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(request, DailyTrainSeat.class);
        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            dailyTrainSeat.setId(IdUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateById(dailyTrainSeat);
        }
    }
    @Override
    public PageResponse<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryRequest request) {
        QueryWrapper<DailyTrainSeat> dailyTrainSeatWrapper = new QueryWrapper<>();
        dailyTrainSeatWrapper.orderByAsc("train_code");
        // 查询条件 按车次查询
        if (ObjectUtil.isNotNull(request.getTrainCode())) {
            dailyTrainSeatWrapper.eq("train_code", request.getTrainCode());
        }
        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectList(dailyTrainSeatWrapper);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeatList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainSeatQueryResponse> list = BeanUtil.copyToList(dailyTrainSeatList, DailyTrainSeatQueryResponse.class);

        PageResponse<DailyTrainSeatQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        dailyTrainSeatMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void generateDailyTrainCode(Date date, String trainCode) {
        log.info("生成日期【{}】车次【{}】的座位信息开始", DateUtil.formatDate(date), trainCode);

        DateTime now = DateTime.now();
        //先删除车次
        QueryWrapper<DailyTrainSeat> wrapper = new QueryWrapper<>();
        wrapper.eq("date", date);
        wrapper.eq("train_code", trainCode);
        dailyTrainSeatMapper.delete(wrapper);

        List<TrainStation> trainStations = trainStationService.getTrainStations(trainCode);
        String sell = StrUtil.fillBefore("", '0', trainStations.size()-1);

        //生成车次
        List<TrainSeat> trainSeats = trainSeatService.getTrainSeat(trainCode);
        if (CollUtil.isEmpty(trainSeats)) {
            log.info("没有车站数据，请先导入车站数据");
            return;
        }
        for (TrainSeat TrainSeat : trainSeats) {
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(TrainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(IdUtil.getSnowflakeNextId());
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setTrainCode(trainCode);
            dailyTrainSeat.setSell(sell);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }
        log.info("生成日期【{}】车次【{}】的座位信息结束", DateUtil.formatDate(date), trainCode);
    }
}

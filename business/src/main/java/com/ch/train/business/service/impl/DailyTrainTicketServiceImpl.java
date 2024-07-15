package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.domain.TrainStation;
import com.ch.train.business.mapper.DailyTrainTicketMapper;
import com.ch.train.business.request.DailyTrainTicketQueryRequest;
import com.ch.train.business.request.DailyTrainTicketSaveRequest;
import com.ch.train.business.response.DailyTrainTicketQueryResponse;
import com.ch.train.business.service.DailyTrainTicketService;
import com.ch.train.business.service.TrainStationService;
import com.ch.train.common.response.PageResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket> implements DailyTrainTicketService {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Resource
    private TrainStationService TrainStationService;

    @Override
    public void save(DailyTrainTicketSaveRequest request) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(request, DailyTrainTicket.class);
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(IdUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateById(dailyTrainTicket);
        }
    }
    @Override
    public PageResponse<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryRequest request) {
        QueryWrapper<DailyTrainTicket> dailyTrainTicketWrapper = new QueryWrapper<>();
        dailyTrainTicketWrapper.orderByDesc("id");

        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectList(dailyTrainTicketWrapper);

        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTicketList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainTicketQueryResponse> list = BeanUtil.copyToList(dailyTrainTicketList, DailyTrainTicketQueryResponse.class);

        PageResponse<DailyTrainTicketQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        dailyTrainTicketMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void generateDailyByTrainCode(Date date, String trainCode) {
        log.info("生成日期【{}】车次【{}】的车票信息开始", DateUtil.formatDate(date), trainCode);

        DateTime now = DateTime.now();
        //先删除车次
        QueryWrapper<DailyTrainTicket> wrapper = new QueryWrapper<>();
        wrapper.eq("date", date);
        wrapper.eq("train_code", trainCode);
        dailyTrainTicketMapper.delete(wrapper);

        //生成车次
        List<TrainStation> trainStations = TrainStationService.getTrainStations(trainCode);
        TrainStation start = new TrainStation();
        TrainStation end = new TrainStation();

        for (int i = 0; i <trainStations.size() ; i++) {
            start = trainStations.get(i);
            for (int j = i+1; j < trainStations.size(); j++) {
                end = trainStations.get(j);
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(IdUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(start.getName());
                dailyTrainTicket.setStartPinyin(start.getNamePinyin());
                dailyTrainTicket.setStartTime(start.getOutTime());
                dailyTrainTicket.setStartIndex(start.getStationIndex());
                dailyTrainTicket.setEnd(end.getName());
                dailyTrainTicket.setEndPinyin(end.getNamePinyin());
                dailyTrainTicket.setEndTime(end.getInTime());
                dailyTrainTicket.setEndIndex(end.getStationIndex());
                dailyTrainTicket.setYdz(0);
                dailyTrainTicket.setYdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setEdz(0);
                dailyTrainTicket.setEdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setRw(0);
                dailyTrainTicket.setRwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setYw(0);
                dailyTrainTicket.setYwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
        log.info("生成日期【{}】车次【{}】的车站信息结束", DateUtil.formatDate(date), trainCode);
    }
}

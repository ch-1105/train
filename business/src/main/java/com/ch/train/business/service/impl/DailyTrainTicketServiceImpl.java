package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.DailyTrain;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.domain.TrainStation;
import com.ch.train.business.enums.SeatTypeEnum;
import com.ch.train.business.enums.TrainTypeEnum;
import com.ch.train.business.mapper.DailyTrainTicketMapper;
import com.ch.train.business.request.DailyTrainTicketQueryRequest;
import com.ch.train.business.request.DailyTrainTicketSaveRequest;
import com.ch.train.business.response.DailyTrainTicketQueryResponse;
import com.ch.train.business.service.DailyTrainSeatService;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket> implements DailyTrainTicketService {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Resource
    private TrainStationService trainStationService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

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
        dailyTrainTicketWrapper.orderByAsc("train_code");

        if (ObjectUtil.isNotNull(request.getDate())) {
            dailyTrainTicketWrapper.eq("date", request.getDate());
        }
        if (ObjectUtil.isNotNull(request.getTrainCode())) {
            dailyTrainTicketWrapper.eq("train_code", request.getTrainCode());
        }
        if (ObjectUtil.isNotNull(request.getStart())) {
            dailyTrainTicketWrapper.eq("start", request.getStart());
        }
        if (ObjectUtil.isNotNull(request.getEnd())) {
            dailyTrainTicketWrapper.eq("end", request.getEnd());
        }

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
    public void generateDailyByTrainCode(DailyTrain dailyTrain,Date date, String trainCode) {
        log.info("生成日期【{}】车次【{}】的车票信息开始", DateUtil.formatDate(date), trainCode);

        DateTime now = DateTime.now();
        //先删除车次
        QueryWrapper<DailyTrainTicket> wrapper = new QueryWrapper<>();
        wrapper.eq("date", date);
        wrapper.eq("train_code", trainCode);
        dailyTrainTicketMapper.delete(wrapper);

        //生成车次
        List<TrainStation> trainStations = trainStationService.getTrainStations(trainCode);
        TrainStation start;
        TrainStation end;

        for (int i = 0; i <trainStations.size() ; i++) {
            start = trainStations.get(i);
            BigDecimal sumKM = BigDecimal.ZERO;
            for (int j = i+1; j < trainStations.size(); j++) {
                end = trainStations.get(j);
                sumKM = sumKM.add(end.getKm());
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
                int ydz = dailyTrainSeatService.countSeatBySeatType(date, trainCode, SeatTypeEnum.YDZ.getCode());
                int edz = dailyTrainSeatService.countSeatBySeatType(date, trainCode, SeatTypeEnum.EDZ.getCode());
                int rw = dailyTrainSeatService.countSeatBySeatType(date, trainCode, SeatTypeEnum.RW.getCode());
                int yw = dailyTrainSeatService.countSeatBySeatType(date, trainCode, SeatTypeEnum.YW.getCode());
                // 票价计算公式 = 里程(阶梯计算) * 座位类型 * 单价
                //座位类型
                String trainType = dailyTrain.getType();
                //座位类型系数
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                //保留两位
                BigDecimal ydzPrice = sumKM.multiply(priceRate).multiply(SeatTypeEnum.YDZ.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKM.multiply(priceRate).multiply(SeatTypeEnum.EDZ.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = sumKM.multiply(priceRate).multiply(SeatTypeEnum.RW.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKM.multiply(priceRate).multiply(SeatTypeEnum.YW.getPrice()).setScale(2, RoundingMode.HALF_UP);
                dailyTrainTicket.setYdz(ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rw);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(yw);
                dailyTrainTicket.setYwPrice(ywPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
        log.info("生成日期【{}】车次【{}】的车站信息结束", DateUtil.formatDate(date), trainCode);
    }

    @Override
    public DailyTrainTicket selectByUnique(String trainCode, Date date, String start, String end) {
        QueryWrapper<DailyTrainTicket> wrapper = new QueryWrapper<>();
        wrapper.eq("train_code", trainCode);
        wrapper.eq("date", date);
        wrapper.eq("start", start);
        wrapper.eq("end", end);
        return  dailyTrainTicketMapper.selectOne(wrapper);
    }
}

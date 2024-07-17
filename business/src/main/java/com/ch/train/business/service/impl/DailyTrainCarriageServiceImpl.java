package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.DailyTrainCarriage;
import com.ch.train.business.domain.TrainCarriage;
import com.ch.train.business.enums.SeatColEnum;
import com.ch.train.business.mapper.DailyTrainCarriageMapper;
import com.ch.train.business.request.DailyTrainCarriageQueryRequest;
import com.ch.train.business.request.DailyTrainCarriageSaveRequest;
import com.ch.train.business.response.DailyTrainCarriageQueryResponse;
import com.ch.train.business.service.DailyTrainCarriageService;
import com.ch.train.business.service.TrainCarriageService;
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
public class DailyTrainCarriageServiceImpl extends ServiceImpl<DailyTrainCarriageMapper, DailyTrainCarriage> implements DailyTrainCarriageService {

    private static final Logger log = LoggerFactory.getLogger(DailyTrainCarriageService.class);

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    @Override
    public void save(DailyTrainCarriageSaveRequest request) {
        DateTime now = DateTime.now();
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(request, DailyTrainCarriage.class);

        // 自动计算列数和车厢总数
        String seatType = request.getSeatType();
        List<SeatColEnum> colsByType = SeatColEnum.getColsByType(seatType);
        int colCount = colsByType.size();
        // 计算车厢总数
        Integer rowCount = request.getRowCount();
        int carriageCount = rowCount * colCount;
        //保存
        dailyTrainCarriage.setSeatCount(carriageCount);
        dailyTrainCarriage.setColCount(colCount);

        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            dailyTrainCarriage.setId(IdUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateById(dailyTrainCarriage);
        }
    }
    @Override
    public PageResponse<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryRequest request) {
        QueryWrapper<DailyTrainCarriage> dailyTrainCarriageWrapper = new QueryWrapper<>();
        dailyTrainCarriageWrapper.orderByDesc("date");
        dailyTrainCarriageWrapper.orderByAsc("train_code");
        // 查询条件 按日期 或 车次查询
        if (ObjectUtil.isNotNull(request.getDate())) {
            dailyTrainCarriageWrapper.eq("date", request.getDate());
        }
        if (ObjectUtil.isNotEmpty(request.getTrainCode())) {
            dailyTrainCarriageWrapper.eq("train_code", request.getTrainCode());
        }
        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectList(dailyTrainCarriageWrapper);

        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainCarriageQueryResponse> list = BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageQueryResponse.class);

        PageResponse<DailyTrainCarriageQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteById(id);
    }

    @Transactional
    public void generateDailyTrainCode(Date date, String trainCode) {
        log.info("生成日期【{}】车次【{}】的车厢信息开始", DateUtil.formatDate(date), trainCode);

        DateTime now = DateTime.now();
        //先删除车次
        QueryWrapper<DailyTrainCarriage> wrapper = new QueryWrapper<>();
        wrapper.eq("date", date);
        wrapper.eq("train_code", trainCode);
        dailyTrainCarriageMapper.delete(wrapper);

        //生成车次
        List<TrainCarriage> trainCarriages = trainCarriageService.getTrainCarriage(trainCode);
        if (CollUtil.isEmpty(trainCarriages)) {
            log.info("没有车站数据，请先导入车站数据");
            return;
        }
        for (TrainCarriage trainCarriage : trainCarriages) {
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(IdUtil.getSnowflakeNextId());
            dailyTrainCarriage.setDate(date);
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setTrainCode(trainCode);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        }
        log.info("生成日期【{}】车次【{}】的车厢信息结束", DateUtil.formatDate(date), trainCode);
    }

    @Override
    public List<DailyTrainCarriage> getByTrainType(String trainCode, Date date,String seatType) {
        QueryWrapper<DailyTrainCarriage> wrapper = new QueryWrapper<>();
        wrapper.eq("train_code", trainCode);
        wrapper.eq("seat_type", seatType);
        wrapper.eq("date", date);
        return dailyTrainCarriageMapper.selectList(wrapper);
    }
}

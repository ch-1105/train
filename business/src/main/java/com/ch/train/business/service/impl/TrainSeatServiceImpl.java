package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.TrainCarriage;
import com.ch.train.business.domain.TrainSeat;
import com.ch.train.business.enums.SeatColEnum;
import com.ch.train.business.enums.SeatTypeEnum;
import com.ch.train.business.mapper.TrainSeatMapper;
import com.ch.train.business.request.TrainSeatQueryRequest;
import com.ch.train.business.request.TrainSeatSaveRequest;
import com.ch.train.business.response.TrainSeatQueryResponse;
import com.ch.train.business.service.TrainCarriageService;
import com.ch.train.business.service.TrainSeatService;
import com.ch.train.common.response.PageResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat> implements TrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainSeatService.class);

    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    public void save(TrainSeatSaveRequest request) {
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(request, TrainSeat.class);
        if (ObjectUtil.isNull(trainSeat.getId())) {
            trainSeat.setId(IdUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
        } else {
            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateById(trainSeat);
        }
    }

    public PageResponse<TrainSeatQueryResponse> queryList(TrainSeatQueryRequest request) {
        QueryWrapper<TrainSeat> trainSeatWrapper = new QueryWrapper<>();
        trainSeatWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<TrainSeat> trainSeatList = trainSeatMapper.selectList(trainSeatWrapper);

        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeatList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainSeatQueryResponse> list = BeanUtil.copyToList(trainSeatList, TrainSeatQueryResponse.class);

        PageResponse<TrainSeatQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        trainSeatMapper.deleteById(id);
    }

    @Transactional
    public void generatorTrainSeat( String trainCode){
        DateTime now = DateTime.now();
        // 0.清空车次座位
        trainSeatMapper.deleteByTrainCode(trainCode);
        // 1.根据 查询车厢列表
        QueryWrapper<TrainCarriage> carriageQueryWrapper = new QueryWrapper<>();
        carriageQueryWrapper.eq("train_code", trainCode);
        List<TrainCarriage> list = trainCarriageService.list(carriageQueryWrapper);
        // 2.进行循环插入
        for (TrainCarriage trainCarriage : list) {
            // 每车厢座位索引
            int seatIndex = 1;
            //1.根据每车厢查询行号 列号 车次类型 插入数据
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            //2.根据车次类型 查询座位列表
            SeatTypeEnum seatEnum = SeatTypeEnum.getEnumByCode(seatType);
            List<SeatColEnum> colsEnum = SeatColEnum.getColsByType(seatType);
            //3.循环行
            for (int i = 1; i <= rowCount; i++) {
                //循环列
                for (SeatColEnum seatColEnum : colsEnum) {
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(IdUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(i), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatEnum.getCode());
                    trainSeat.setCarriageIndex(trainCarriage.getCarriageIndex());
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    trainSeatMapper.insert(trainSeat);
                }
            }
        }
    }

    @Override
    public List<TrainSeat> getTrainSeat(String trainCode) {
        QueryWrapper<TrainSeat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("train_code", trainCode);
        return trainSeatMapper.selectList(queryWrapper);
    }
}

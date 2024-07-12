package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.DailyTrainSeat;
import com.ch.train.business.mapper.DailyTrainSeatMapper;
import com.ch.train.business.service.DailyTrainSeatService;
import com.ch.train.business.request.DailyTrainSeatQueryRequest;
import com.ch.train.business.request.DailyTrainSeatSaveRequest;
import com.ch.train.business.response.DailyTrainSeatQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat> implements DailyTrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

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
        dailyTrainSeatWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectList(dailyTrainSeatWrapper);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeatList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

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
}

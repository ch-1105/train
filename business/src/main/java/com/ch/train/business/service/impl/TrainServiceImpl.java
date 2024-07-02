package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.Train;
import com.ch.train.business.mapper.TrainMapper;
import com.ch.train.business.service.TrainService;
import com.ch.train.business.request.TrainQueryRequest;
import com.ch.train.business.request.TrainSaveRequest;
import com.ch.train.business.response.TrainQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    @Resource
    private TrainMapper trainMapper;

    public void save(TrainSaveRequest request) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(request, Train.class);
        if (ObjectUtil.isNull(train.getId())) {
            train.setId(IdUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateById(train);
        }
    }

    public PageResponse<TrainQueryResponse> queryList(TrainQueryRequest request) {
        QueryWrapper<Train> trainWrapper = new QueryWrapper<>();
        trainWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Train> trainList = trainMapper.selectList(trainWrapper);

        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainQueryResponse> list = BeanUtil.copyToList(trainList, TrainQueryResponse.class);

        PageResponse<TrainQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }

    public void delete(Long id) {
        trainMapper.deleteById(id);
    }

    @Override
    public List<TrainQueryResponse> queryAllTrain() {
        QueryWrapper<Train> trainWrapper = new QueryWrapper<>();
        //根据车次编号排列
        trainWrapper.orderByDesc("code");

        List<Train> trainList = trainMapper.selectList(trainWrapper);

        return BeanUtil.copyToList(trainList, TrainQueryResponse.class);
    }

}

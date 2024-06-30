package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.TrainCarriage;
import com.ch.train.business.mapper.TrainCarriageMapper;
import com.ch.train.business.service.TrainCarriageService;
import com.ch.train.business.request.TrainCarriageQueryRequest;
import com.ch.train.business.request.TrainCarriageSaveRequest;
import com.ch.train.business.response.TrainCarriageQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class TrainCarriageServiceImpl extends ServiceImpl<TrainCarriageMapper, TrainCarriage> implements TrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);

    @Resource
    private TrainCarriageMapper trainCarriageMapper;

    public void save(TrainCarriageSaveRequest request) {
        DateTime now = DateTime.now();
        TrainCarriage trainCarriage = BeanUtil.copyProperties(request, TrainCarriage.class);
        if (ObjectUtil.isNull(trainCarriage.getId())) {
            trainCarriage.setId(IdUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateById(trainCarriage);
        }
    }

    public PageResponse<TrainCarriageQueryResponse> queryList(TrainCarriageQueryRequest request) {
        QueryWrapper<TrainCarriage> trainCarriageWrapper = new QueryWrapper<>();
        trainCarriageWrapper.orderByDesc("id");

        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectList(trainCarriageWrapper);

        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainCarriageQueryResponse> list = BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResponse.class);

        PageResponse<TrainCarriageQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }

    public void delete(Long id) {
        trainCarriageMapper.deleteById(id);
    }
}

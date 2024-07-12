package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ch.train.business.enums.SeatColEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.domain.DailyTrainCarriage;
import com.ch.train.business.mapper.DailyTrainCarriageMapper;
import com.ch.train.business.service.DailyTrainCarriageService;
import com.ch.train.business.request.DailyTrainCarriageQueryRequest;
import com.ch.train.business.request.DailyTrainCarriageSaveRequest;
import com.ch.train.business.response.DailyTrainCarriageQueryResponse;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class DailyTrainCarriageServiceImpl extends ServiceImpl<DailyTrainCarriageMapper, DailyTrainCarriage> implements DailyTrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

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
        LOG.info("查询页码：{}", request.getPageNum());
        LOG.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectList(dailyTrainCarriageWrapper);

        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

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
}

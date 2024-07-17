package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.ConfirmOrder;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.enums.ConfirmOrderStatusEnum;
import com.ch.train.business.enums.SeatTypeEnum;
import com.ch.train.business.mapper.ConfirmOrderMapper;
import com.ch.train.business.request.ConfirmOrderQueryRequest;
import com.ch.train.business.request.ConfirmOrderSaveRequest;
import com.ch.train.business.request.ConfirmOrderTicketRequest;
import com.ch.train.business.response.ConfirmOrderQueryResponse;
import com.ch.train.business.service.ConfirmOrderService;
import com.ch.train.business.service.DailyTrainTicketService;
import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.utils.GlobalException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder> implements ConfirmOrderService {

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @Override
    public void save(ConfirmOrderSaveRequest request) {
        orderTicket(request);
    }

    private boolean orderTicket(ConfirmOrderSaveRequest request) {
        // 业务校验 判断票数>0 车票、车次、座位存在，且车票、车次、座位状态为可用 乘车人是否购买过同一张票等

        DateTime now = DateTime.now();
        Date date = request.getDate();
        String trainCode = request.getTrainCode();
        String start = request.getStart();
        String end = request.getEnd();

        // 获取余票
        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(IdUtil.getSnowflakeNextId());
        confirmOrder.setMemberId(Long.valueOf(LoginMemberContext.getId()));
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(request.getDailyTrainTicketId());
        confirmOrder.setTickets(JSON.toJSONString(request.getTickets()));
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrderMapper.insert(confirmOrder);
        // 预扣存
        DailyTrainTicket dailyTrainTicket =
                dailyTrainTicketService.selectByUnique(trainCode, date, start, end);
        log.info("余票信息：{}", dailyTrainTicket);
        for (ConfirmOrderTicketRequest ticket: request.getTickets()) {
            String seatTypeCode = ticket.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
            reduceTicket(seatTypeEnum, dailyTrainTicket);
        }
        // 选座
        
            // 每车厢循环获取座位是否可选

            // 多个选座应该在同一车箱

        // 选中后事务

            // 座位表更新数量
            // 余票表更新状态
            // 订单表更新状态
        return true;
    }

    private static void reduceTicket(SeatTypeEnum seatTypeEnum, DailyTrainTicket dailyTrainTicket) {
        switch (seatTypeEnum) {
            //jdk17写法
            case YDZ->{
                int countYdz = dailyTrainTicket.getYdz() - 1;
                if (countYdz <0) {
                    throw new GlobalException("YDZ余票不足");
                }
                dailyTrainTicket.setYdz(countYdz);
            }

            case EDZ->{
                int countEdz = dailyTrainTicket.getEdz() - 1;
                if (countEdz <0) {
                    throw new GlobalException("EDZ余票不足");
                }
                dailyTrainTicket.setEdz(countEdz);
            }
            case RW->{
                int countRw = dailyTrainTicket.getRw() - 1;
                if (countRw <0) {
                    throw new GlobalException("RW余票不足");
                }
                dailyTrainTicket.setRw(countRw);
            }
            case YW->{
                int countYw = dailyTrainTicket.getYw() - 1;
                if (countYw <0) {
                    throw new GlobalException("YW余票不足");
                }
                dailyTrainTicket.setYw(countYw);
            }
        }
    }

    @Override
    public PageResponse<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryRequest request) {
        QueryWrapper<ConfirmOrder> confirmOrderWrapper = new QueryWrapper<>();
        confirmOrderWrapper.orderByDesc("id");

        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectList(confirmOrderWrapper);

        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResponse> list = BeanUtil.copyToList(confirmOrderList, ConfirmOrderQueryResponse.class);

        PageResponse<ConfirmOrderQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        confirmOrderMapper.deleteById(id);
    }


}

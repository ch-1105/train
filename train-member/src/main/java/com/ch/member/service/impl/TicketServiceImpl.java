package com.ch.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.member.domain.Ticket;
import com.ch.member.mapper.TicketMapper;
import com.ch.member.request.MemberTicketQueryRequest;
import com.ch.member.request.TicketQueryRequest;
import com.ch.member.response.TicketQueryResponse;
import com.ch.member.service.TicketService;
import com.ch.train.common.request.MemberTicketRequest;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.utils.GlobalException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    @Resource
    private TicketMapper ticketMapper;

    @Override
    public void save(MemberTicketRequest request) {
        log.info("seata全局事务id : {}", RootContext.getXID());
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(request, Ticket.class);
        ticket.setId(IdUtil.getSnowflakeNextId());
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        ticketMapper.insert(ticket);
        // 模拟超时可以在数据库中查看 branch语句
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        // 模拟异常
//        throw new RuntimeException("模拟异常");
    }
    @Override
    public PageResponse<TicketQueryResponse> queryList(TicketQueryRequest request) {
        QueryWrapper<Ticket> ticketWrapper = new QueryWrapper<>();
        ticketWrapper.orderByDesc("id");

        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Ticket> ticketList = ticketMapper.selectList(ticketWrapper);

        PageInfo<Ticket> pageInfo = new PageInfo<>(ticketList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<TicketQueryResponse> list = BeanUtil.copyToList(ticketList, TicketQueryResponse.class);

        PageResponse<TicketQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
    @Override
    public void delete(Long id) {
        ticketMapper.deleteById(id);
    }

    @Override
    public PageResponse<TicketQueryResponse> queryMemberList(MemberTicketQueryRequest request) {
        QueryWrapper<Ticket> ticketWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNull(request.getMemberId())) {
            throw new GlobalException(401, "会员ID不能为空");
        }
        ticketWrapper.eq("member_id", request.getMemberId());
        ticketWrapper.orderByDesc("id");
        log.info("查询页码：{}", request.getPageNum());
        log.info("每页条数：{}", request.getPageSize());
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Ticket> ticketList = ticketMapper.selectList(ticketWrapper);

        PageInfo<Ticket> pageInfo = new PageInfo<>(ticketList);
        log.info("总行数：{}", pageInfo.getTotal());
        log.info("总页数：{}", pageInfo.getPages());

        List<TicketQueryResponse> list = BeanUtil.copyToList(ticketList, TicketQueryResponse.class);

        PageResponse<TicketQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(list);
        return pageResponse;
    }
}

package com.ch.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.train.business.domain.ConfirmOrder;
import com.ch.train.business.domain.DailyTrainCarriage;
import com.ch.train.business.domain.DailyTrainSeat;
import com.ch.train.business.domain.DailyTrainTicket;
import com.ch.train.business.enums.ConfirmOrderStatusEnum;
import com.ch.train.business.enums.SeatColEnum;
import com.ch.train.business.enums.SeatTypeEnum;
import com.ch.train.business.mapper.ConfirmOrderMapper;
import com.ch.train.business.request.ConfirmOrderQueryRequest;
import com.ch.train.business.request.ConfirmOrderSaveRequest;
import com.ch.train.business.request.ConfirmOrderTicketRequest;
import com.ch.train.business.response.ConfirmOrderQueryResponse;
import com.ch.train.business.service.ConfirmOrderService;
import com.ch.train.business.service.DailyTrainCarriageService;
import com.ch.train.business.service.DailyTrainSeatService;
import com.ch.train.business.service.DailyTrainTicketService;
import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.utils.GlobalException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder> implements ConfirmOrderService {

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderService.class);

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;
    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;
    @Resource
    private AfterConfirmOrderService afterConfirmOrderService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    @Override
    public void save(ConfirmOrderSaveRequest request) {
        orderTicket(request);
    }

    @SentinelResource(value="save" , blockHandler="orderTicketBlockHandler")
    public boolean orderTicket(ConfirmOrderSaveRequest request) {
        String lockKey = request.getTrainCode() + request.getDate();
//        Boolean setIfAbsent = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockKey , 5 , TimeUnit.SECONDS);
//
//        if (Boolean.TRUE.equals(setIfAbsent)) {
//            log.info("redis锁 获取成功 , {}", lockKey);
//        }else {
//            log.info("redis锁 获取失败 , {}", lockKey);
//            throw new GlobalException("当前购票人数过多，请稍后再试");
//        }

        // 声明锁
        RLock rLock = null;
        // 业务校验 判断票数>0 车票、车次、座位存在，且车票、车次、座位状态为可用 乘车人是否购买过同一张票等
        try {
            // 使用redisson获取锁,自带了看门狗
            rLock = redissonClient.getLock(lockKey);
            /**
             * wait time 等待获取锁的时间
             * lease time 锁多久
             */
//            boolean b = rLock.tryLock(0,10, TimeUnit.SECONDS); wait , lease , unit
            boolean b = rLock.tryLock(0, TimeUnit.SECONDS); // 因为有看门狗

            if (b) {
            log.info("redis锁 获取成功 , {}", lockKey);
            }else {
                log.info("redis锁 获取失败 , {}", lockKey);
                throw new GlobalException("当前购票人数过多，请稍后再试");
            }

            DateTime now = DateTime.now();
            Date date = request.getDate();
            String trainCode = request.getTrainCode();
            String start = request.getStart();
            String end = request.getEnd();
            List<ConfirmOrderTicketRequest> tickets = request.getTickets();

            List<DailyTrainSeat> systemChooseedList = new ArrayList<>();

            // 获取余票
            ConfirmOrder confirmOrder = new ConfirmOrder();
            confirmOrder.setId(IdUtil.getSnowflakeNextId());
            confirmOrder.setMemberId(Long.valueOf(LoginMemberContext.getId()));
            confirmOrder.setDate(date);
            confirmOrder.setTrainCode(trainCode);
            confirmOrder.setStart(start);
            confirmOrder.setEnd(end);
            confirmOrder.setDailyTrainTicketId(request.getDailyTrainTicketId());
            confirmOrder.setTickets(JSON.toJSONString(tickets));
            confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            confirmOrderMapper.insert(confirmOrder);
            // 预扣存
            DailyTrainTicket dailyTrainTicket =
                    dailyTrainTicketService.selectByUnique(trainCode, date, start, end);
            log.info("余票信息：{}", dailyTrainTicket);
            for (ConfirmOrderTicketRequest ticket: tickets) {
                String seatTypeCode = ticket.getSeatTypeCode();
                SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);
                reduceTicket(seatTypeEnum, dailyTrainTicket);
            }
            // 选座 这里需要计算偏移值，c1 - d2 【0 - 5】 座位偏移值以0开始计算,没有选座就直接跳过
            ConfirmOrderTicketRequest ticketReq0 = tickets.get(0);
            if (StrUtil.isNotBlank(ticketReq0.getSeat())) {
                List<SeatColEnum> cols = SeatColEnum.getColsByType(ticketReq0.getSeatTypeCode());
                log.info("本次选座的座位信息：{}", cols);
                ArrayList<String> optionList = new ArrayList<>();
                // 组成两排座位进行参照 eg:list([a1,b1,c1,d1,a2,b2,c2,d2])
                for (int i = 0; i < 2; i++) {
                    for (SeatColEnum col : cols) {
                        optionList.add(col.getCode() + (i+1));
                    }
                }
                log.info("座位参照信息：{}", optionList);
                // 计算绝对偏移值
                ArrayList<Integer> absOffsetList = new ArrayList<>();
                for (ConfirmOrderTicketRequest ticket : tickets) {
                    String seat = ticket.getSeat();
                    int index = optionList.indexOf(seat);
                    absOffsetList.add(index);
                }
                log.info("座位绝对偏移值：{}", absOffsetList);
                // 计算相对偏移值
                ArrayList<Integer> relOffsetList = new ArrayList<>();
                for (Integer i : absOffsetList) {
                    relOffsetList.add(i - absOffsetList.get(0));
                }
                getSeat(systemChooseedList,
                        trainCode,
                        date,
                        ticketReq0.getSeatTypeCode(),
                        ticketReq0.getSeat().split("")[0], //这里将A1 取A
                        relOffsetList,
                        dailyTrainTicket.getStartIndex(),
                        dailyTrainTicket.getEndIndex());
                log.info("座位相对偏移值(与第一个座位的偏移值)：{}", relOffsetList);
            }else{
                log.info("本次无选择座位,数据为:{}", tickets);
                // 没选座,所以循环车票获取类型
                for (ConfirmOrderTicketRequest ticket: tickets) {
                    getSeat(systemChooseedList,
                            trainCode,
                            date,
                            ticket.getSeatTypeCode(),
                            null,// 没有选座
                            null,
                            dailyTrainTicket.getStartIndex(),
                            dailyTrainTicket.getEndIndex());
                }
            }
            log.info("最终选座信息：{}", systemChooseedList);

            // 更新数据库
            try {
                afterConfirmOrderService.doConfirmOrder(dailyTrainTicket,systemChooseedList,tickets,confirmOrder);
            } catch (Exception e) {
                log.error("订单处理异常", e);
                throw new GlobalException("订单处理异常");
            }
        } catch (NumberFormatException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 解锁 只有当前线程才能解锁
            if(null != rLock  && rLock.isHeldByCurrentThread()) {
                log.info("投票结束 , 释放锁 , {} " , lockKey);
                rLock.unlock();
            }
        }
//        stringRedisTemplate.delete(lockKey);
    return true;
    }

    public boolean orderTicketBlockHandler(ConfirmOrderSaveRequest request, BlockException e) {
        log.info("当前请求被限流 , {} " , request);
        throw new GlobalException("当前购票人数过多,请求被限流，请稍后再试");
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

    private List<DailyTrainSeat> getSeat(List<DailyTrainSeat> systemChooseedList,
                                   String trainCode,
                                   Date date,String seatType,
                                   String column,
                                   List<Integer> offSetList,
                                   int start,
                                   int end){
        List<DailyTrainSeat> tempChooseList;
        List<DailyTrainCarriage> carriages = dailyTrainCarriageService.getByTrainType(trainCode, date, seatType);
        log.info("获取车箱数量：{}", carriages.size());
        // 每车厢循环获取座位是否可选
        // 多个选座应该在同一车箱
        for (DailyTrainCarriage carriage : carriages) {
            tempChooseList = new ArrayList<>();
            log.info("几号车厢开始选座：{}", carriage.getCarriageIndex());
            List<DailyTrainSeat> seats =
                    dailyTrainSeatService.getByTrainCarriageIndex(trainCode, date, carriage.getCarriageIndex());
            log.info("车箱座位数量：{}", seats.size());
            boolean chooseAllSeat = true;
            for (DailyTrainSeat seat : seats) {
                // 判断列号进行选座
                boolean blank = StrUtil.isBlank(column);
                String col = seat.getCol();
                Integer seatIndex = seat.getCarriageSeatIndex();
                if (blank) {
                    log.info("当前选择座位列号为空，直接返回：{}", seat);
                }else{
                    // 不等于时直接跳过
                    if (!StrUtil.equals(col, column)) {
                        log.info("当前座位列号不匹配，跳过：{}", seat.getCol());
                        continue;
                    }
                }

                // 判断当前座位是否被选择
                boolean systemChoosed = false;
                for (DailyTrainSeat dailyTrainSeat : systemChooseedList) {
                    if (seat.getId().equals(dailyTrainSeat.getId())){
                        log.info("座位已被选择，跳过：{}", seat);
                        systemChoosed = true;
                        break;
                    }
                }
                if (systemChoosed) {
                    log.info("座位已被选择，跳过");
                    continue;
                }
                boolean isChoose = chooseSeat(seat,start,
                        end);
                if (isChoose) {
                    log.info("座位可售：{}", seat);
                    tempChooseList.add(seat);
                }else {
                    log.info("座位不可售：{}", seat);
                    continue;
                }
                // 根据相对偏移值进行选座
                if (CollUtil.isNotEmpty(offSetList)) {
                    log.info("当前需要选择的偏移值列表：{}", offSetList);
                    for (int i = 1; i < offSetList.size(); i++) {//从1开始 0是当前座位
                        int offset = offSetList.get(i);
                        //所以这里-1是为了让相对位置的座位从1开始,因为这里是0开始,座位index是1开始
                        int nextSeat = seatIndex + offset - 1;
                        // 获取相对位置的座位
                        if (nextSeat > seats.size() - 1) {
                            log.info("座位超出范围，跳过：{}", nextSeat);
                            chooseAllSeat = false;
                            break;
                        }

                        DailyTrainSeat dailyTrainSeat = seats.get(nextSeat);
                        // 判断列号进行选座
                        boolean nextChoose = chooseSeat(dailyTrainSeat,start,
                                end);
                        if (nextChoose) {
                            log.info("下一个座位可售：{}", dailyTrainSeat);
                            tempChooseList.add(dailyTrainSeat);
                        }else {
                            log.info("下一个座位不可选：{}", dailyTrainSeat);
                            chooseAllSeat = false;
                        }
                    }
                }
                if (!chooseAllSeat) {
                    tempChooseList.clear();
                    continue;
                }
                systemChooseedList.addAll(tempChooseList);
                return systemChooseedList;
            }
        }
        return null;
    }

    /**
     * 计算座位在某区间是否可选 sell为0可选 1不可选
     * @date 2024/7/18 8:42
     * @param dailyTrainSeat
     * @return boolean
     */
    private boolean chooseSeat(DailyTrainSeat dailyTrainSeat,int start,int end){
        String sell = dailyTrainSeat.getSell();
        String sellPart = sell.substring(start, end);
        if (Integer.parseInt(sellPart) > 0){
            log.info("座位不可选 : 座位：{} - 区间 {} -> {}", dailyTrainSeat,start,end);
            return false;
        } else{
            log.info("座位可选 : 座位：{} - 区间 {} -> {}", dailyTrainSeat,start,end);
            String currentSell = sellPart.replace("0", "1");
            currentSell = StrUtil.fillBefore(currentSell, '0', end);
            currentSell = StrUtil.fillAfter(currentSell, '0', sell.length());
            int temp = NumberUtil.binaryToInt(currentSell) | NumberUtil.binaryToInt(sell);
            String newSell = NumberUtil.getBinaryStr(temp);
            // 全1的情况下补全首位0
            newSell = StrUtil.fillBefore(newSell, '0', sell.length());
            log.info("座位可选 : 座位：{} - 区间 {} -> {} - 计算后售票信息：{}", dailyTrainSeat,start,end,newSell);
            dailyTrainSeat.setSell(newSell);
            return true;
        }
    }

    public static void main(String[] args) {
        DailyTrainSeat dailyTrainSeat = new DailyTrainSeat();
        dailyTrainSeat.setSell("000000");
        ConfirmOrderServiceImpl impl = new ConfirmOrderServiceImpl();
        boolean b = impl.chooseSeat(dailyTrainSeat, 1, 4);
    }

    @Override
    public void delete(Long id) {
        confirmOrderMapper.deleteById(id);
    }
}

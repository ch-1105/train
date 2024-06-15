package com.ch.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.common.context.LoginMemberContext;
import com.ch.common.resp.PageResponse;
import com.ch.member.domain.Passenger;
import com.ch.member.mapper.PassengerMapper;
import com.ch.member.request.PassengerQueryRequest;
import com.ch.member.request.PassengerSaveRequest;
import com.ch.member.responce.PassengerQueryResponse;
import com.ch.member.service.PassengerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ch051
* @description 针对表【passenger(乘车人)】的数据库操作Service实现
* @createDate 2024-06-10 10:27:19
*/
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
    implements PassengerService {
    private static final Logger log = LoggerFactory.getLogger(PassengerServiceImpl.class);
    @Resource
    PassengerMapper passengerMapper;

    @Override
    public void savePassenger(PassengerSaveRequest passenger) {
        DateTime now = DateTime.now();
        Passenger savePassenger = BeanUtil.copyProperties(passenger,
                Passenger.class);
        //根据传入passenger确定时新增还是更新
        if (ObjectUtil.isNull(passenger.getId())) {
            savePassenger.setMemberId(Long.valueOf(LoginMemberContext.getId()));
            savePassenger.setId(IdUtil.getSnowflakeNextId());
            savePassenger.setCreateTime(now);
            savePassenger.setUpdateTime(now);
            passengerMapper.insert(savePassenger);
        }else{
            savePassenger.setUpdateTime(now);
            passengerMapper.updateById(savePassenger);
        }
    }

    @Override
    public PageResponse<PassengerQueryResponse> getPassengerList(PassengerQueryRequest passenger) {
        QueryWrapper<Passenger> passengerQueryWrapper = new QueryWrapper<>();
        //service设计成通用，为以后拓展

        if (ObjectUtil.isNotNull(passenger.getMemberId())) {
            passengerQueryWrapper.eq("member_id",
                    passenger.getMemberId());
        }
        passengerQueryWrapper.orderByDesc("id");

        log.info("查询页码 : {}" , passenger.getPageNum());
        log.info("每页条数 : {}" , passenger.getPageSize());
        PageHelper.startPage(passenger.getPageNum(), passenger.getPageSize());

        List<Passenger> passengerList = passengerMapper.selectList(passengerQueryWrapper);

        PageInfo<Passenger> info = new PageInfo<>(passengerList);
        log.info("总条数 : {}" , info.getTotal());
        log.info("总页数 : {}" , info.getPages());
        //封装返回结果
        List<PassengerQueryResponse> responseListlist = BeanUtil.copyToList(passengerList, PassengerQueryResponse.class);

        PageResponse<PassengerQueryResponse> resp = new PageResponse<>();
        resp.setList(responseListlist);
        resp.setTotal(info.getTotal());

        return resp;
    }


    @Override
    public void deletePassenger(Long id) {
        passengerMapper.deleteById(id);
    }
}





package com.ch.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.common.context.LoginMemberContext;
import com.ch.member.domain.Passenger;
import com.ch.member.mapper.PassengerMapper;
import com.ch.member.request.PassengerQueryRequest;
import com.ch.member.request.PassengerSaveRequest;
import com.ch.member.service.PassengerService;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
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
    @Resource
    PassengerMapper passengerMapper;

    @Override
    public void savePassenger(PassengerSaveRequest passenger) {
        DateTime now = DateTime.now();
        Passenger savePassenger = BeanUtil.copyProperties(passenger,
                Passenger.class);
        savePassenger.setMemberId(Long.valueOf(LoginMemberContext.getId()));
        savePassenger.setId(IdUtil.getSnowflakeNextId());
        savePassenger.setCreateTime(now);
        savePassenger.setUpdateTime(now);
        passengerMapper.insert(savePassenger);
    }

    @Override
    public List<Passenger> getPassengerList(PassengerQueryRequest passenger) {
        QueryWrapper<Passenger> passengerQueryWrapper = new QueryWrapper<>();
        //service设计成通用，为以后拓展

        if (ObjectUtil.isNotNull(passenger.getMemberId())) {
            passengerQueryWrapper.eq("member_id",
                    passenger.getMemberId());
        }
        PageHelper.startPage(2,2);
        List<Passenger> passengers = passengerMapper.selectList(passengerQueryWrapper);
        return passengers;
    }
}





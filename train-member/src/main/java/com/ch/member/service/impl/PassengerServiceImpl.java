package com.ch.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.member.domain.Passenger;
import com.ch.member.mapper.PassengerMapper;
import com.ch.member.service.PassengerService;
import org.springframework.stereotype.Service;

/**
* @author ch051
* @description 针对表【passenger(乘车人)】的数据库操作Service实现
* @createDate 2024-06-10 10:27:19
*/
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
    implements PassengerService {

}





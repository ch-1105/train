package com.ch.member.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.member.domain.Passenger;
import com.ch.member.request.PassengerSaveRequest;

/**
* @author ch051
* @description 针对表【passenger(乘车人)】的数据库操作Service
* @createDate 2024-06-10 10:27:19
*/
public interface PassengerService extends IService<Passenger> {

    void savePassenger(PassengerSaveRequest passenger);
}

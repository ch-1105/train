package com.ch.member.controller;

import com.ch.common.context.LoginMemberContext;
import com.ch.common.result.Result;
import com.ch.member.domain.Passenger;
import com.ch.member.request.PassengerQueryRequest;
import com.ch.member.request.PassengerSaveRequest;
import com.ch.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author: ch
 * create: 2024--2415:18
 * Description:
 */
@RestController()
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    PassengerService passengerService;

    @PostMapping("/save")
    public Result<Object> savePassenger(@RequestBody @Valid PassengerSaveRequest passenger){
        passengerService.savePassenger(passenger);
        return Result.success();
    }

    @GetMapping ("/getList")
    public Result<Object> getPassengerList(@Valid PassengerQueryRequest passenger){
        passenger.setMemberId(Long.valueOf(LoginMemberContext.getId()));
        List<Passenger> passengers= passengerService.getPassengerList(passenger);
        return Result.success(passengers);
    }
}

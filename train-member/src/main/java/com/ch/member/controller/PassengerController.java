package com.ch.member.controller;

import com.ch.common.result.Result;
import com.ch.member.request.PassengerSaveRequest;
import com.ch.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

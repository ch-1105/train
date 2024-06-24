package com.ch.member.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import com.ch.member.request.PassengerQueryRequest;
import com.ch.member.request.PassengerSaveRequest;
import com.ch.member.responce.PassengerQueryResponse;
import com.ch.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageResponse<PassengerQueryResponse>> getPassengerList(@Valid PassengerQueryRequest passenger){
        passenger.setMemberId(Long.valueOf(LoginMemberContext.getId()));
        PageResponse<PassengerQueryResponse> passengerList = passengerService.getPassengerList(passenger);
        return Result.success(passengerList);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> deletePassenger(@PathVariable Long id){
        passengerService.deletePassenger(id);
        return Result.success();
    }
}

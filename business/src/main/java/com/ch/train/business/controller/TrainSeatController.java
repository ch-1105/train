package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.TrainSeatQueryRequest;
import com.ch.train.business.request.TrainSeatSaveRequest;
import com.ch.train.business.response.TrainSeatQueryResponse;
import com.ch.train.business.service.TrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatController {

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody TrainSeatSaveRequest request) {
        trainSeatService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<TrainSeatQueryResponse>> queryList(@Valid TrainSeatQueryRequest request) {
        PageResponse<TrainSeatQueryResponse> list = trainSeatService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return Result.success();
    }

}

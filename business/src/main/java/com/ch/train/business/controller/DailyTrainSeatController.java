package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.DailyTrainSeatQueryRequest;
import com.ch.train.business.request.DailyTrainSeatSaveRequest;
import com.ch.train.business.response.DailyTrainSeatQueryResponse;
import com.ch.train.business.service.DailyTrainSeatService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatController {

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainSeatSaveRequest request) {
        dailyTrainSeatService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<DailyTrainSeatQueryResponse>> queryList(@Valid DailyTrainSeatQueryRequest request) {
        PageResponse<DailyTrainSeatQueryResponse> list = dailyTrainSeatService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainSeatService.delete(id);
        return Result.success();
    }

}

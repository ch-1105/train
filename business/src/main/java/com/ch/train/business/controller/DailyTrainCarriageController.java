package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.DailyTrainCarriageQueryRequest;
import com.ch.train.business.request.DailyTrainCarriageSaveRequest;
import com.ch.train.business.response.DailyTrainCarriageQueryResponse;
import com.ch.train.business.service.DailyTrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageController {

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainCarriageSaveRequest request) {
        dailyTrainCarriageService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<DailyTrainCarriageQueryResponse>> queryList(@Valid DailyTrainCarriageQueryRequest request) {
        PageResponse<DailyTrainCarriageQueryResponse> list = dailyTrainCarriageService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainCarriageService.delete(id);
        return Result.success();
    }

}

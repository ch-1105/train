package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.DailyTrainStationQueryRequest;
import com.ch.train.business.request.DailyTrainStationSaveRequest;
import com.ch.train.business.response.DailyTrainStationQueryResponse;
import com.ch.train.business.service.DailyTrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainStationSaveRequest request) {
        dailyTrainStationService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<DailyTrainStationQueryResponse>> queryList(@Valid DailyTrainStationQueryRequest request) {
        PageResponse<DailyTrainStationQueryResponse> list = dailyTrainStationService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainStationService.delete(id);
        return Result.success();
    }

}

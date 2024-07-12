package com.ch.train.business.controller;

import com.ch.train.business.request.DailyTrainQueryRequest;
import com.ch.train.business.request.DailyTrainSaveRequest;
import com.ch.train.business.response.DailyTrainQueryResponse;
import com.ch.train.business.service.DailyTrainService;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainController {

    @Resource
    private DailyTrainService dailyTrainService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainSaveRequest request) {
        dailyTrainService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<DailyTrainQueryResponse>> queryList(@Valid DailyTrainQueryRequest request) {
        PageResponse<DailyTrainQueryResponse> list = dailyTrainService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainService.delete(id);
        return Result.success();
    }

    @GetMapping ("/gen-daily/{date}")
    public Result<Object> generateDailyTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.generateBatchDailyTrain(date);
        return Result.success();
    }
}

package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.TrainStationQueryRequest;
import com.ch.train.business.request.TrainStationSaveRequest;
import com.ch.train.business.response.TrainStationQueryResponse;
import com.ch.train.business.service.TrainStationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationController {

    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody TrainStationSaveRequest request) {
        trainStationService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<TrainStationQueryResponse>> queryList(@Valid TrainStationQueryRequest request) {
        PageResponse<TrainStationQueryResponse> list = trainStationService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return Result.success();
    }

}

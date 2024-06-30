package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.TrainCarriageQueryRequest;
import com.ch.train.business.request.TrainCarriageSaveRequest;
import com.ch.train.business.response.TrainCarriageQueryResponse;
import com.ch.train.business.service.TrainCarriageService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageController {

    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody TrainCarriageSaveRequest request) {
        trainCarriageService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<TrainCarriageQueryResponse>> queryList(@Valid TrainCarriageQueryRequest request) {
        PageResponse<TrainCarriageQueryResponse> list = trainCarriageService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        trainCarriageService.delete(id);
        return Result.success();
    }

}

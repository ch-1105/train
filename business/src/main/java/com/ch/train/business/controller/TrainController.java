package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.TrainQueryRequest;
import com.ch.train.business.request.TrainSaveRequest;
import com.ch.train.business.response.TrainQueryResponse;
import com.ch.train.business.service.TrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody TrainSaveRequest request) {
        trainService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<TrainQueryResponse>> queryList(@Valid TrainQueryRequest request) {
        PageResponse<TrainQueryResponse> list = trainService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        trainService.delete(id);
        return Result.success();
    }

}

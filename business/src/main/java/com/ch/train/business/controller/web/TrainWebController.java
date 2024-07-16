package com.ch.train.business.controller.web;

import com.ch.train.business.request.TrainQueryRequest;
import com.ch.train.business.response.TrainQueryResponse;
import com.ch.train.business.service.TrainService;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/passenger/train")
public class TrainWebController {

    @Resource
    private TrainService trainService;

    @GetMapping("/query-list")
    public Result<PageResponse<TrainQueryResponse>> queryList(@Valid TrainQueryRequest request) {
        PageResponse<TrainQueryResponse> list = trainService.queryList(request);
        return Result.success(list);
    }

    @GetMapping("/query-all-train")
    public Result<List<TrainQueryResponse>> queryAllTrain() {
        List<TrainQueryResponse> list = trainService.queryAllTrain();
        return Result.success(list);
    }
}

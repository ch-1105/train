package com.ch.train.business.controller.web;

import com.ch.train.business.request.StationQueryRequest;
import com.ch.train.business.response.StationQueryResponse;
import com.ch.train.business.service.StationService;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger/station")
public class StationWebController {

    @Resource
    private StationService stationService;

    @GetMapping("/query-list")
    public Result<PageResponse<StationQueryResponse>> queryList(@Valid StationQueryRequest request) {
        PageResponse<StationQueryResponse> list = stationService.queryList(request);
        return Result.success(list);
    }

    @GetMapping("/query-all-station")
    public Result<List<StationQueryResponse>> queryAll() {
        List<StationQueryResponse> list = stationService.queryAll();
        return Result.success(list);
    }
}

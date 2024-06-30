package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.StationQueryRequest;
import com.ch.train.business.request.StationSaveRequest;
import com.ch.train.business.response.StationQueryResponse;
import com.ch.train.business.service.StationService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/station")
public class StationController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody StationSaveRequest request) {
        stationService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<StationQueryResponse>> queryList(@Valid StationQueryRequest request) {
        PageResponse<StationQueryResponse> list = stationService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        stationService.delete(id);
        return Result.success();
    }

}

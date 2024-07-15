package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.DailyTrainTicketQueryRequest;
import com.ch.train.business.request.DailyTrainTicketSaveRequest;
import com.ch.train.business.response.DailyTrainTicketQueryResponse;
import com.ch.train.business.service.DailyTrainTicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody DailyTrainTicketSaveRequest request) {
        dailyTrainTicketService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<DailyTrainTicketQueryResponse>> queryList(@Valid DailyTrainTicketQueryRequest request) {
        PageResponse<DailyTrainTicketQueryResponse> list = dailyTrainTicketService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        dailyTrainTicketService.delete(id);
        return Result.success();
    }

}

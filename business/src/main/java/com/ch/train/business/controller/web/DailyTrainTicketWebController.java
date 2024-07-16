package com.ch.train.business.controller.web;

import com.ch.train.business.request.DailyTrainTicketQueryRequest;
import com.ch.train.business.response.DailyTrainTicketQueryResponse;
import com.ch.train.business.service.DailyTrainTicketService;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger/daily-train-ticket")
public class DailyTrainTicketWebController {

    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    @GetMapping("/query-list")
    public Result<PageResponse<DailyTrainTicketQueryResponse>> queryList(@Valid DailyTrainTicketQueryRequest request) {
        PageResponse<DailyTrainTicketQueryResponse> list = dailyTrainTicketService.queryList(request);
        return Result.success(list);
    }
}

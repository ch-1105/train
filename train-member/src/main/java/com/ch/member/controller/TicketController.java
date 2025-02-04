package com.ch.member.controller;

import com.ch.member.request.TicketQueryRequest;
import com.ch.member.response.TicketQueryResponse;
import com.ch.member.service.TicketService;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ticket")
public class TicketController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public Result<PageResponse<TicketQueryResponse>> queryList(@Valid TicketQueryRequest request) {
        PageResponse<TicketQueryResponse> list = ticketService.queryList(request);
        return Result.success(list);
    }

}

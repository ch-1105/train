package com.ch.member.controller;

import com.ch.member.request.MemberTicketQueryRequest;
import com.ch.member.response.TicketQueryResponse;
import com.ch.member.service.TicketService;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class MemberTicketController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public Result<PageResponse<TicketQueryResponse>> queryList(@Valid MemberTicketQueryRequest request) {
        PageResponse<TicketQueryResponse> list = ticketService.queryMemberList(request);
        return Result.success(list);
    }
}

package com.ch.member.controller.feign;

import com.ch.member.request.TicketQueryRequest;
import com.ch.member.response.TicketQueryResponse;
import com.ch.member.service.TicketService;
import com.ch.train.common.request.MemberTicketRequest;
import com.ch.train.common.response.PageResponse;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/ticket")
public class FeignTicketController {

    @Resource
    private TicketService ticketService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody MemberTicketRequest request) {
        ticketService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<TicketQueryResponse>> queryList(@Valid TicketQueryRequest request) {
        PageResponse<TicketQueryResponse> list = ticketService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return Result.success();
    }

}

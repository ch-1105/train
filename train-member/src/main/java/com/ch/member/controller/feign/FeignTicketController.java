package com.ch.member.controller.feign;

import com.ch.member.service.TicketService;
import com.ch.train.common.request.MemberTicketRequest;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class FeignTicketController {

    @Resource
    private TicketService ticketService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody MemberTicketRequest request) {
        ticketService.save(request);
        return Result.success();
    }
}

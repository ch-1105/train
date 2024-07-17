package com.ch.train.business.controller.web;

import com.ch.train.business.request.ConfirmOrderSaveRequest;
import com.ch.train.business.service.ConfirmOrderService;
import com.ch.train.common.result.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger/confirm-order")
public class ConfirmOrderWebController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody ConfirmOrderSaveRequest request) {
        confirmOrderService.save(request);
        return Result.success();
    }
}

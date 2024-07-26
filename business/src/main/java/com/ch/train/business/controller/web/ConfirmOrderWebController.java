package com.ch.train.business.controller.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ch.train.business.request.ConfirmOrderSaveRequest;
import com.ch.train.business.service.ConfirmOrderService;
import com.ch.train.common.result.Result;
import com.ch.train.common.utils.GlobalException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger/confirm-order")
public class ConfirmOrderWebController {
    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderWebController.class);

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    @SentinelResource(value="saveOrder" , blockHandler="orderTicketBlockHandler")
    public Result<Object> save(@Valid @RequestBody ConfirmOrderSaveRequest request) {
        confirmOrderService.save(request);
        return Result.success();
    }

    public Result<Object> orderTicketBlockHandler(ConfirmOrderSaveRequest request, BlockException e) {
        log.info("当前请求被限流 , {} " , request);
        throw new GlobalException("当前购票人数过多,请求被限流，请稍后再试");
    }
}

package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.ConfirmOrderQueryRequest;
import com.ch.train.business.request.ConfirmOrderSaveRequest;
import com.ch.train.business.response.ConfirmOrderQueryResponse;
import com.ch.train.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/confirm-order")
public class ConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody ConfirmOrderSaveRequest request) {
        confirmOrderService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<ConfirmOrderQueryResponse>> queryList(@Valid ConfirmOrderQueryRequest request) {
        PageResponse<ConfirmOrderQueryResponse> list = confirmOrderService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        confirmOrderService.delete(id);
        return Result.success();
    }

}

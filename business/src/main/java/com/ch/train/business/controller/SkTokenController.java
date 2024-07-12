package com.ch.train.business.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.business.request.SkTokenQueryRequest;
import com.ch.train.business.request.SkTokenSaveRequest;
import com.ch.train.business.response.SkTokenQueryResponse;
import com.ch.train.business.service.SkTokenService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/sk-token")
public class SkTokenController {

    @Resource
    private SkTokenService skTokenService;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody SkTokenSaveRequest request) {
        skTokenService.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<SkTokenQueryResponse>> queryList(@Valid SkTokenQueryRequest request) {
        PageResponse<SkTokenQueryResponse> list = skTokenService.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        skTokenService.delete(id);
        return Result.success();
    }

}

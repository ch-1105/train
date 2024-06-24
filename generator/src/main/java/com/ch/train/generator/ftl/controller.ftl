package com.ch.train.${module}.controller;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.result.Result;
import com.ch.train.common.response.PageResponse;
import com.ch.train.${module}.request.${Domain}QueryRequest;
import com.ch.train.${module}.request.${Domain}SaveRequest;
import com.ch.train.${module}.response.${Domain}QueryResponse;
import com.ch.train.${module}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/${do_main}")
public class ${Domain}Controller {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public Result<Object> save(@Valid @RequestBody ${Domain}SaveRequest request) {
        ${domain}Service.save(request);
        return Result.success();
    }

    @GetMapping("/query-list")
    public Result<PageResponse<${Domain}QueryResponse>> queryList(@Valid ${Domain}QueryRequest request) {
        PageResponse<${Domain}QueryResponse> list = ${domain}Service.queryList(request);
        return Result.success(list);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return Result.success();
    }

}

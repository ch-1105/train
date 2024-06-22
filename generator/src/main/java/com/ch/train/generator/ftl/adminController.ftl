package com.ch.train.${module}.controller.admin;

import com.ch.train.common.context.LoginMemberContext;
import com.ch.train.common.response.CommonResponse;
import com.ch.train.common.response.PageResponse;
import com.ch.train.${module}.request.${Domain}QueryRequestuest;
import com.ch.train.${module}.request.${Domain}SaveRequestuest;
import com.ch.train.${module}.response.${Domain}QueryResponseonse;
import com.ch.train.${module}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestuestMapping("/admin/${do_main}")
public class ${Domain}AdminController {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResponse<Object> save(@Valid @RequestuestBody ${Domain}SaveRequestuest request) {
        ${domain}Service.save(request);
        return new CommonResponse<>();
    }

    @GetMapping("/query-list")
    public CommonResponse<PageResponse<${Domain}QueryResponseonse>> queryList(@Valid ${Domain}QueryRequestuest request) {
        PageResponse<${Domain}QueryResponseonse> list = ${domain}Service.queryList(request);
        return new CommonResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return new CommonResponse<>();
    }

}

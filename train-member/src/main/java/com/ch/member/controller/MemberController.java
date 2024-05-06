package com.ch.member.controller;

import cn.hutool.core.util.StrUtil;
import com.ch.common.result.Result;
import com.ch.common.utils.GlobalException;
import com.ch.member.request.MemberRequest;
import com.ch.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ch
 * create: 2024--2415:18
 * Description:
 */
@RestController()
@RequestMapping()
public class MemberController {
    @Resource
    private MemberService memberService;
    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/register")
    public Result<Long> registerMember(MemberRequest member){
        if (StrUtil.isBlank(member.getMobile())) {
            throw new GlobalException("手机号不能为空");
        }
        return Result.success(memberService.registerMember(member));
    }
}

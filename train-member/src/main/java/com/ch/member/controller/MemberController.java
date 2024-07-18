package com.ch.member.controller;

import com.ch.train.common.result.Result;
import com.ch.member.request.MemberLoginRequest;
import com.ch.member.request.MemberRequest;
import com.ch.member.response.MemberLoginResponce;
import com.ch.member.service.MemberService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public Result<String> test(){
        return Result.success("test");
    }

    @PostMapping("/register")
    
    public Result<Long> registerMember(@Valid @RequestBody MemberRequest member){
        return Result.success(memberService.registerMember(member));
    }

    @PostMapping("/sendCode")
    public Result<Object> sendCode(@Valid @RequestBody MemberRequest member){
        memberService.sendCode(member);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<MemberLoginResponce> login(@Valid @RequestBody MemberLoginRequest member){
        return Result.success(memberService.login(member));
    }
}

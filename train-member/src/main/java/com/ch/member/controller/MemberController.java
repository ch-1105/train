package com.ch.member.controller;

import com.ch.common.result.Result;
import com.ch.member.request.MemberLoginRequest;
import com.ch.member.request.MemberRequest;
import com.ch.member.responce.MemberLoginResponce;
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
    public String test(){
        return "test";
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

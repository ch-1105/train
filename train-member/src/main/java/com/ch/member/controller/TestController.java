package com.ch.member.controller;

import com.ch.member.service.MemberMapperService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ch
 * create: 2024--2415:18
 * Description:
 */
@RestController()
@RequestMapping()
public class TestController {
    @Resource
    private MemberMapperService memberMapperService;
    @GetMapping()
    public String test(){
        return "test";
    }

    @GetMapping("/count")
    public int getMember(){
        return memberMapperService.getMember();
    }
}

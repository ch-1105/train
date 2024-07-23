package com.ch.member.controller;

import com.ch.train.common.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * author: ch
 * create: 2024--1914:31
 * Description:
 */
@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController {
    @Value("${test.nacos}")
    private String test;

    @GetMapping("/nacos-test")
    public Result<String> test(){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        System.out.println("test : "+test);
        return Result.success(test);
    }
}

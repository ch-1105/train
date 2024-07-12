package com.ch.train.timer.controller;

import com.ch.train.timer.feign.BusinessFeign;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ch
 * create: 2024--1110:06
 * Description:
 */
@RestController
@RequestMapping("/timer")
public class TestController {

    @Resource
    private BusinessFeign businessFeign;
    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @RequestMapping("/hello")
    public void hello(){
        System.out.println(businessFeign.list());
    }
}

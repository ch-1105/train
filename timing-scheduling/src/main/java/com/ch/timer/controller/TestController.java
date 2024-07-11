package com.ch.timer.controller;

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
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}

package com.ch.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ch
 * create: 2024--2415:18
 * Description:
 */
@RestController()
@RequestMapping("/test")
public class TestController {
    @GetMapping("/test2")
    public String test(){
        return "test";
    }
}

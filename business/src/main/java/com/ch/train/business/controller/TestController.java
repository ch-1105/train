package com.ch.train.business.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ch.train.common.utils.GlobalException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: ch
 * create: 2024--2611:11
 * Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/hello")
    @SentinelResource(value = "HelloWorld", blockHandler = "block")
    public String test() throws InterruptedException {
        Thread.sleep(100);
        return "hello";
    }

    public String block(BlockException e) throws InterruptedException {
        Thread.sleep(100);
        throw new GlobalException("hello 测试 降级");
    }
}

package com.ch.train.timer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * author: ch
 * create: 2024--1214:17
 * Description:
 */
@FeignClient(name = "business", url = "http://localhost:8082/business")
public interface BusinessFeign {

    @GetMapping("/admin/station/query-all-station")
    Object list();
}

package com.ch.train.timer.feign;

import com.ch.train.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * author: ch
 * create: 2024--1214:17
 * Description:
 */
@FeignClient(name = "business")
//@FeignClient(name = "business", url = "http://localhost:8082/business")
public interface BusinessFeign {

    @GetMapping("/business/admin/station/query-all-station")
    Object list();

    @GetMapping ("/business/admin/daily-train/gen-daily/{date}")
    Result<Object> generateDailyTrain(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}

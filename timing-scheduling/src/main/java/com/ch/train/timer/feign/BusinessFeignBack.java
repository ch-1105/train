package com.ch.train.timer.feign;

import com.ch.train.common.result.Result;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * author: ch
 * create: 2024--2615:19
 * Description:
 */
@Component
public class BusinessFeignBack implements BusinessFeign {
    private static final Logger log = LoggerFactory.getLogger(BusinessFeignBack.class);
    @Override
    public Object list() {
        log.info("BusinessFeignBack");
        return "fallback";
    }
    @Override
    public Result<Object> generateDailyTrain(Date date) {
        return null;
    }
}
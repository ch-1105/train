package com.ch.train.business.request;

import com.ch.train.common.request.PageRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyTrainSeatQueryRequest extends PageRequest {
    private String trainCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}

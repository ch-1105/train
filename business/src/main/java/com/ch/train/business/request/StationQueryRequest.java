package com.ch.train.business.request;

import com.ch.train.common.request.PageRequest;
import lombok.Data;

@Data
public class StationQueryRequest extends PageRequest {

    @Override
    public String toString() {
        return "StationQueryRequest{" +
                "} " + super.toString();
    }
}

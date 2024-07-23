package com.ch.train.business.feign;

import com.ch.train.common.request.MemberTicketRequest;
import com.ch.train.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "member")
//@FeignClient(name = "train-member" , url = "http://127.0.0.1:8080")
public interface MemberFeign {
    @PostMapping("/member/ticket/save")
    Result<Object> save(MemberTicketRequest request);
}

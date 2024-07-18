package com.ch.member.request;

import com.ch.train.common.request.PageRequest;
import lombok.Data;

@Data
public class MemberTicketQueryRequest extends PageRequest {
    private Long memberId;
}

package com.ch.member.request;

import com.ch.train.common.request.PageRequest;
import lombok.Data;

/**
 * 乘车人
 * @TableName passenger
 */
@Data
public class PassengerQueryRequest extends PageRequest {

    /**
     * 会员id
     */
    private Long memberId;

}
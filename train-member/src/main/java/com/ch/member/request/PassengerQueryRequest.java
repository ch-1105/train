package com.ch.member.request;

import com.ch.train.common.request.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 乘车人列表查询
 */
@Data
public class PassengerQueryRequest extends PageRequest implements Serializable {

    private Long memberId;
}
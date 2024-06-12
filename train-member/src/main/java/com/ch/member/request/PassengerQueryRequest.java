package com.ch.member.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 乘车人列表查询
 */
@Data
public class PassengerQueryRequest implements Serializable {

    private Long memberId;
}
package com.ch.member.responce;

import lombok.Data;

import java.io.Serializable;

/**
 * 乘车人列表查询
 */
@Data
public class PassengerQueryResponse implements Serializable {

    private String name;

    private String idCard;

    private String type;
}
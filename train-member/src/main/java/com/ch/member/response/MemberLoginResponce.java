package com.ch.member.response;

import lombok.Data;

import java.io.Serializable;

/**
 * member.`member`
 * @TableName member
 */
@Data
public class MemberLoginResponce implements Serializable {

    public String id;

    public String mobile;

    public String token;
}
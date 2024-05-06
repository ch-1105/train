package com.ch.member.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * member.`member`
 * @TableName member
 */
@Data
public class MemberRequest{
    @NotBlank(message = "手机号不能为空！")
    private String mobile;
}
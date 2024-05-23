package com.ch.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * member.`member`
 * @TableName member
 */
@Data
public class MemberRequest{

    @NotBlank(message ="【手机号】不能为空")
    @Pattern(regexp="^1\\d{10}$",message ="手机号码格式错误")
    private String mobile;
}
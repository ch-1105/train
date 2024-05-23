package com.ch.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

/**
 * member.`member`
 * @TableName member
 */
@Data
public class MemberLoginRequest implements Serializable {
    @NotBlank(message ="【手机号】不能为空")
    @Pattern(regexp="^1\\d{10}$",message ="手机号码格式错误")
    public String mobile;

    @NotBlank(message ="【验证码】不能为空")
    public String code;
}
package com.ch.member.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 乘车人
 * @TableName passenger
 */
@Data
public class PassengerSaveRequest implements Serializable {

    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 姓名
     */
    @NotBlank(message ="【姓名】不能为空")
    private String name;

    /**
     * 身份证
     */
    @NotBlank(message ="【身份证】不能为空")
    private String idCard;

    /**
     * 旅客类型|枚举[PassengerTypeEnum]
     */
    @NotBlank(message ="【旅客类型】不能为空")
    private String type;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
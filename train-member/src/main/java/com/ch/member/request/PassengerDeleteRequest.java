package com.ch.member.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 乘车人
 * @TableName passenger
 */
@Data
public class PassengerDeleteRequest implements Serializable {

    @NotNull(message ="【id】不能为空")
    private Long id;

}
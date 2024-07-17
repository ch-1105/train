package com.ch.train.business.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmOrderTicketRequest {
    //   对应前端传值
    //   passengerId: 123,
    //   passengerType: "1",
    //   passengerName: "张三",
    //   passengerIdCard: "12323132132",
    //   seatTypeCode: "1",
    //   seat: "C1"

    @NotNull(message = "【购票人id】不能为空")
    private Long passengerId;

    @NotNull(message = "【购票人类型】不能为空")
    private Long passengerType;

    @NotBlank(message = "【购票人名称】不能为空")
    private String passengerName;

    @NotBlank(message = "【购票人身份证】不能为空")
    private String passengerIdCard;

    @NotBlank(message = "【座位类型】不能为空")
    private String seatTypeCode;

    @NotBlank(message = "【座位编号】不能为空")
    private String seat;


}

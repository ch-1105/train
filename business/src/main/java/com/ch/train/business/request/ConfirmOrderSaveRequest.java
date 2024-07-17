package com.ch.train.business.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ConfirmOrderSaveRequest {

    @NotNull(message = "【会员id】不能为空")
    private Long memberId;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private Date date;

    @NotBlank(message = "【车次编号】不能为空")
    private String trainCode;

    @NotBlank(message = "【出发站】不能为空")
    private String start;

    @NotBlank(message = "【到达站】不能为空")
    private String end;

    @NotNull(message = "【余票ID】不能为空")
    private Long dailyTrainTicketId;

    @NotBlank(message = "【车票】不能为空")
    private List<ConfirmOrderTicketRequest> tickets;
}

package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 确认订单
 * @TableName confirm_order
 */
@TableName(value ="confirm_order")
@Data
public class ConfirmOrder implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    private Long memberId;

    /**
     * 日期
     */
    @TableField(value = "date")
    private Date date;

    /**
     * 车次编号
     */
    @TableField(value = "train_code")
    private String trainCode;

    /**
     * 出发站
     */
    @TableField(value = "start")
    private String start;

    /**
     * 到达站
     */
    @TableField(value = "end")
    private String end;

    /**
     * 余票ID
     */
    @TableField(value = "daily_train_ticket_id")
    private Long dailyTrainTicketId;

    /**
     * 车票
     */
    @TableField(value = "tickets")
    private Object tickets;

    /**
     * 订单状态|枚举[ConfirmOrderStatusEnum]
     */
    @TableField(value = "status")
    private String status;

    /**
     * 新增时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
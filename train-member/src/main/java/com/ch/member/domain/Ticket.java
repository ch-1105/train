package com.ch.member.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车票
 * @TableName ticket
 */
@TableName(value ="ticket")
@Data
public class Ticket implements Serializable {
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
     * 乘客id
     */
    @TableField(value = "passenger_id")
    private Long passengerId;

    /**
     * 乘客姓名
     */
    @TableField(value = "passenger_name")
    private String passengerName;

    /**
     * 日期
     */
    @TableField(value = "train_date")
    private Date trainDate;

    /**
     * 车次编号
     */
    @TableField(value = "train_code")
    private String trainCode;

    /**
     * 箱序
     */
    @TableField(value = "carriage_index")
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    @TableField(value = "seat_row")
    private String seatRow;

    /**
     * 列号|枚举[SeatColEnum]
     */
    @TableField(value = "seat_col")
    private String seatCol;

    /**
     * 出发站
     */
    @TableField(value = "start_station")
    private String startStation;

    /**
     * 出发时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 到达站
     */
    @TableField(value = "end_station")
    private String endStation;

    /**
     * 到站时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    @TableField(value = "seat_type")
    private String seatType;

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
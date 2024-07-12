package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日座位
 * @TableName daily_train_seat
 */
@TableName(value ="daily_train_seat")
@Data
public class DailyTrainSeat implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

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
     * 箱序
     */
    @TableField(value = "carriage_index")
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    @TableField(value = "`row`")
    private String row;

    /**
     * 列号|枚举[SeatColEnum]
     */
    @TableField(value = "col")
    private String col;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    @TableField(value = "seat_type")
    private String seatType;

    /**
     * 同车箱座序
     */
    @TableField(value = "carriage_seat_index")
    private Integer carriageSeatIndex;

    /**
     * 售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖
     */
    @TableField(value = "sell")
    private String sell;

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
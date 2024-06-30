package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 座位
 * @TableName train_seat
 */
@TableName(value ="train_seat")
@Data
public class TrainSeat implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 车次编号
     */
    @TableField(value = "train_code")
    private String trainCode;

    /**
     * 厢序
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
    @TableField(value = "`col`")
    private String col;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    @TableField(value = "seat_type")
    private String seatType;

    /**
     * 同车厢座序
     */
    @TableField(value = "carriage_seat_index")
    private Integer carriageSeatIndex;

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
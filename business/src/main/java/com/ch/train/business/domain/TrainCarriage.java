package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 火车车厢
 * @TableName train_carriage
 */
@TableName(value ="train_carriage")
@Data
public class TrainCarriage implements Serializable {
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
     * 厢号
     */
    @TableField(value = "carriage_index")
    private Integer carriageIndex;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    @TableField(value = "seat_type")
    private String seatType;

    /**
     * 座位数
     */
    @TableField(value = "seat_count")
    private Integer seatCount;

    /**
     * 排数
     */
    @TableField(value = "row_count")
    private Integer rowCount;

    /**
     * 列数
     */
    @TableField(value = "col_count")
    private Integer colCount;

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
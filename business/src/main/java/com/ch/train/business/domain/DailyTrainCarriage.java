package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 每日车厢
 * @TableName daily_train_carriage
 */
@TableName(value ="daily_train_carriage")
@Data
public class DailyTrainCarriage implements Serializable {
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
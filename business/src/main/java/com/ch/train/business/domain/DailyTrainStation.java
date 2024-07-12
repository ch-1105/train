package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 每日车站
 * @TableName daily_train_station
 */
@TableName(value ="daily_train_station")
@Data
public class DailyTrainStation implements Serializable {
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
     * 站序|第一站是0
     */
    @TableField(value = "index")
    private Integer index;

    /**
     * 站名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 站名拼音
     */
    @TableField(value = "name_pinyin")
    private String namePinyin;

    /**
     * 进站时间
     */
    @TableField(value = "in_time")
    private Date inTime;

    /**
     * 出站时间
     */
    @TableField(value = "out_time")
    private Date outTime;

    /**
     * 停站时长
     */
    @TableField(value = "stop_time")
    private Date stopTime;

    /**
     * 里程（公里）|从上一站到本站的距离
     */
    @TableField(value = "km")
    private BigDecimal km;

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
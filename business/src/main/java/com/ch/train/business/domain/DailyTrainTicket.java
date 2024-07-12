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
 * 余票信息
 * @TableName daily_train_ticket
 */
@TableName(value ="daily_train_ticket")
@Data
public class DailyTrainTicket implements Serializable {
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
     * 出发站
     */
    @TableField(value = "start")
    private String start;

    /**
     * 出发站拼音
     */
    @TableField(value = "start_pinyin")
    private String startPinyin;

    /**
     * 出发时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 出发站序|本站是整个车次的第几站
     */
    @TableField(value = "start_index")
    private Integer startIndex;

    /**
     * 到达站
     */
    @TableField(value = "end")
    private String end;

    /**
     * 到达站拼音
     */
    @TableField(value = "end_pinyin")
    private String endPinyin;

    /**
     * 到站时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 到站站序|本站是整个车次的第几站
     */
    @TableField(value = "end_index")
    private Integer endIndex;

    /**
     * 一等座余票
     */
    @TableField(value = "ydz")
    private Integer ydz;

    /**
     * 一等座票价
     */
    @TableField(value = "ydz_price")
    private BigDecimal ydzPrice;

    /**
     * 二等座余票
     */
    @TableField(value = "edz")
    private Integer edz;

    /**
     * 二等座票价
     */
    @TableField(value = "edz_price")
    private BigDecimal edzPrice;

    /**
     * 软卧余票
     */
    @TableField(value = "rw")
    private Integer rw;

    /**
     * 软卧票价
     */
    @TableField(value = "rw_price")
    private BigDecimal rwPrice;

    /**
     * 硬卧余票
     */
    @TableField(value = "yw")
    private Integer yw;

    /**
     * 硬卧票价
     */
    @TableField(value = "yw_price")
    private BigDecimal ywPrice;

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
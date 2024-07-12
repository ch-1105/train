package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 每日车次
 * @TableName daily_train
 */
@TableName(value ="daily_train")
@Data
public class DailyTrain implements Serializable {
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
    @TableField(value = "code")
    private String code;

    /**
     * 车次类型|枚举[TrainTypeEnum]
     */
    @TableField(value = "type")
    private String type;

    /**
     * 始发站
     */
    @TableField(value = "start")
    private String start;

    /**
     * 始发站拼音
     */
    @TableField(value = "start_pinyin")
    private String startPinyin;

    /**
     * 出发时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 终点站
     */
    @TableField(value = "end")
    private String end;

    /**
     * 终点站拼音
     */
    @TableField(value = "end_pinyin")
    private String endPinyin;

    /**
     * 到站时间
     */
    @TableField(value = "end_time")
    private Date endTime;

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
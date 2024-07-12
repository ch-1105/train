package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 秒杀令牌
 * @TableName sk_token
 */
@TableName(value ="sk_token")
@Data
public class SkToken implements Serializable {
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
     * 令牌余量
     */
    @TableField(value = "count")
    private Integer count;

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
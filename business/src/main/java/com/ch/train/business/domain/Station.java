package com.ch.train.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车站
 * @TableName station
 */
@TableName(value ="station")
@Data
public class Station implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 站名
     */
    private String name;

    /**
     * 站名拼音
     */
    @TableField(value = "name_pinyin")
    private String namePinyin;

    /**
     * 站名拼音首字母
     */
    private String namePy;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
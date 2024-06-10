package com.ch.member.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 乘车人
 * @TableName passenger
 */
@TableName(value ="passenger")
@Data
public class Passenger implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 会员id
     */
    private Long member_id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String id_card;

    /**
     * 旅客类型|枚举[PassengerTypeEnum]
     */
    private String type;

    /**
     * 新增时间
     */
    private Date create_time;

    /**
     * 修改时间
     */
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.ch.common.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * author: ch
 * create: 2024--1512:16
 * Description:
 */
@Data
public class PageResponse<T> implements Serializable {
    //总条数
    private Long total;

    //返回的封装列表
    private List<T> list;
}

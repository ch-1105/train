package com.ch.train.common.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * author: ch
 * create: 2024--1512:16
 * Description:
 */
@Data
public class PageRequest {
    @NotNull(message="[分页页码]不能为空")
    private Integer pageNum;

    @NotNull(message="[分页数量]不能为空")
    @Max(value = 100, message = "[分页数量]不能超过100")
    private Integer pageSize;
}

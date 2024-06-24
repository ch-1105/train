package com.ch.train.common.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * author: ch
 * create: 2024--0610:15
 * Description:
 */
@Getter
@Setter
public class GlobalException extends RuntimeException {

    private int code; // 自定义错误码
    private String message; // 错误信息

    public GlobalException(String message) {
        super(message); // 调用父类构造器传递错误信息
        this.code = 400;
        this.message = message;
    }
    public GlobalException(int code, String message) {
        super(message); // 调用父类构造器传递错误信息
        this.code = code;
        this.message = message;
    }

    public GlobalException(String message, Throwable cause, int code) {
        super(message, cause); // 包含cause，用于保存异常链
        this.code = code;
    }

    public GlobalException(Throwable cause, int code) {
        super(cause); // 直接传入cause
        this.code = code;
    }

    public GlobalException(String message, int code, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // 完整构造器
        this.code = code;
    }
}
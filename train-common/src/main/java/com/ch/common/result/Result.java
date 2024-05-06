package com.ch.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: ch
 * create: 2024--069:58
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code; // 状态码，200表示成功，非200表示失败
    private String message; // 返回信息，成功或错误的具体信息
    private T data; // 数据对象，泛型T表示返回的数据类型

    // 成功静态方法
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败静态方法
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    enum ResultCode {
        SUCCESS(200, "成功"),
        FAIL(400, "失败"),
        UNAUTHORIZED(401, "未认证"),
        NOT_FOUND(404, "接口不存在"),
        INTERNAL_SERVER_ERROR(500, "服务器内部错误");

        private Integer code;
        private String message;

        ResultCode(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}

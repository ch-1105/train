package com.ch.train.common.utils;

import cn.hutool.core.util.StrUtil;
import com.ch.train.common.result.Result;
import io.seata.core.context.RootContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

/**
 * author: ch
 * create: 2024--0610:13
 * Description:
 */
@ControllerAdvice // 该注解表示此类为全局异常处理类
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 处理自定义异常
    @ResponseBody
    @ExceptionHandler(value = {GlobalException.class}) // 捕捉特定的自定义异常
    public Result<Object> handleCustomException(GlobalException ex) {
        // 这里可以对异常进行日志记录等操作
        Result<String> result = new Result<>(ex.getCode(), ex.getMessage(), null);
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    // 处理所有未被捕获的异常
    @ExceptionHandler(value = {Exception.class})
    public Result<String> handleAllExceptions(Exception ex) throws Exception {
        log.info("seata全局事务id : {}", RootContext.getXID());
        // 避免全局异常被包装成 200的调用失败
        if(StrUtil.isNotBlank(RootContext.getXID())) {
            throw ex;
        }
        // 记录日志或者做其他处理
        return new Result<>(500, "服务器内部错误", null);
    }

    // 处理Spring Validation校验不通过时抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                //这里打印出校验不通过的字段和错误信息
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        Result<String> result = new Result<>(400, errorMessage, null);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}

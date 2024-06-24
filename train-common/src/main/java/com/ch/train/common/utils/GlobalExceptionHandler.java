package com.ch.train.common.utils;

import com.ch.train.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * author: ch
 * create: 2024--0610:13
 * Description:
 */
@ControllerAdvice // 该注解表示此类为全局异常处理类
public class GlobalExceptionHandler {

    // 处理自定义异常
    @ExceptionHandler(value = {GlobalException.class}) // 捕捉特定的自定义异常
    public ResponseEntity<Object> handleCustomException(GlobalException ex) {
        // 这里可以对异常进行日志记录等操作
        Result<String> result = new Result<>(ex.getCode(), ex.getMessage(), null);
        return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
    }

    // 处理所有未被捕获的异常
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        // 记录日志或者做其他处理
        Result<String> result = new Result<>(500, "服务器内部错误", null);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
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

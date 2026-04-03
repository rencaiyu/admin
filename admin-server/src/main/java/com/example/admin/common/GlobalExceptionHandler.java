package com.example.admin.common;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBiz(BizException ex) {
        return ApiResponse.fail(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldError() == null ? "参数错误"
                : ex.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.fail(msg);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleEx(Exception ex) {
        return ApiResponse.fail(ex.getMessage());
    }
}

package com.lyq.bookManageSystem.common.exception;

import com.lyq.bookManageSystem.common.response.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseResult handleBusinessException(BusinessException ex) {
        return ResponseResult.error( ex.getMessage(),ex.getCode());
    }

    // 处理参数校验异常（如IllegalArgumentException）
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseResult.error("请求参数错误: " + ex.getMessage(),400);
    }

    //处理未知异常
    @ExceptionHandler(Exception.class)
    public ResponseResult Unknow(Exception ex) {
        return ResponseResult.error("未知错误" ,600);
    }


}

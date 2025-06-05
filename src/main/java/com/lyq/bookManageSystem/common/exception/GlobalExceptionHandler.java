package com.lyq.bookManageSystem.common.exception;

import com.lyq.bookManageSystem.common.response.ResponseResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
//@ControllerAdvice(basePackages = "com.lyq.bookManageSystem.controller")
public class GlobalExceptionHandler {
    //处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseResult<?>>   handleBusinessException(BusinessException ex) {
        ResponseResult response = ResponseResult.error( ex.getMessage(),ex.getCode());
        return ResponseEntity.status(ex.getHttpCode()).body(response);
    }

//    处理DTO类校验的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseResult<?>> handleValidationException(MethodArgumentNotValidException ex) {
        // 提取第一个字段错误信息
        String errorMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseResult.error(errorMsg, HttpStatus.BAD_REQUEST.value()));
    }

//    处理文件上传的异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseResult<?>> handleUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        String msg = "上传文件大小超过限制";
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ResponseResult.error(msg, 40052));
    }


//    处理数据库外键约束抛出的异常
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseResult<?>> handleSQLConstraintException(DataIntegrityViolationException ex) {
        String msg = "数据库约束冲突";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseResult.error(msg, HttpStatus.CONFLICT.value()));
    }



    //处理未知异常
    @ExceptionHandler(Exception.class)
    public ResponseResult Unknow(Exception ex) {

        return ResponseResult.error("未知错误: "+ex.getMessage() ,600);
    }

}

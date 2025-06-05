package com.lyq.bookManageSystem.common.enums;
import org.springframework.http.HttpStatus;

/*
A：用户端错误（如参数错误、支付超时）
B：当前系统错误（如业务逻辑缺陷）
C：第三方服务错误（如CDN故障）
 */

public enum BusinessErrorCode {

    USERNAME_EXIST         (20001,  "用户名已经存在", HttpStatus.BAD_REQUEST.value()),
    ID_LIST_EMPTY          (20002, "ID列表不能为空", HttpStatus.BAD_REQUEST.value()),
    INVALID_ID             (20003,  "未提供有效ID", HttpStatus.BAD_REQUEST.value()),

    ID_MUST_BE_POSITIVE(40001, "ID必须为正数",HttpStatus.BAD_REQUEST.value()),
    INVALID_ID_FORMAT(40002, "无效的ID格式",HttpStatus.BAD_REQUEST.value()),
    ACCOUNT_LOCKED(40003, "账号已被锁定", HttpStatus.UNAUTHORIZED.value()),
    USERNAME_OR_PWD_ERROR(40004, "用户名或密码错误", HttpStatus.UNAUTHORIZED.value()),
    MISSING_AUTHORIZATION_HEADER(40005, "Authorization头缺失或格式错误",HttpStatus.UNAUTHORIZED.value()),
    TOKEN_PARSING_FAILED(40006, "令牌解析失败",HttpStatus.UNAUTHORIZED.value()),

    USER_CREATE_FAILED     (50001,  "新增失败", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    USER_UPDATE_FAILED     (50002,  "修改失败或无需修改", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    USER_DELETE_FAILED     (50003, "删除失败或已被删除", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final int code; // 业务状态码（5位）
    private final String message;   // 错误描述

    private final int httpCode;   // http状态码

    BusinessErrorCode(int code, String message, int httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    // Getter方法
    public int getCode() { return code; }
    public String getMessage() { return message; }

    public int getHttpCode() {
        return httpCode;
    }
}

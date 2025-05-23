package com.lyq.bookManageSystem.common.enums;

public enum BusinessErrorCode {
//    SUCCESS         (20000,  "操作成功"),
    // 客户端错误（4xx语义，编码2开头）
    USERNAME_EXIST         (20001,  "用户名已经存在"),
    ID_LIST_EMPTY          (20002, "ID列表不能为空"),
    INVALID_ID             (20003,  "未提供有效ID"),

    // 服务端错误（5xx语义，编码5开头）
    USER_CREATE_FAILED     (50001,  "用户创建失败"),
    USER_UPDATE_FAILED     (50002,  "用户更新失败"),
    USER_DELETE_FAILED     (50003, "用户删除失败");

    private final int code; // 业务状态码（5位）
    private final String message;   // 错误描述

    BusinessErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter方法
    public int getCode() { return code; }
    public String getMessage() { return message; }
}

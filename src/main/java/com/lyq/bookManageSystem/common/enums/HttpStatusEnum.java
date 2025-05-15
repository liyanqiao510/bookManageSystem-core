package com.lyq.bookManageSystem.common.enums;
import lombok.Getter;
import lombok.Setter;


public enum HttpStatusEnum {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "系统异常");

    private final int code;
    private final String defaultMsg;

    // constructor & getters

    HttpStatusEnum(int code, String defaultMsg) {
        this.code = code;
        this.defaultMsg = defaultMsg;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMsg() {
        return defaultMsg;
    }
}

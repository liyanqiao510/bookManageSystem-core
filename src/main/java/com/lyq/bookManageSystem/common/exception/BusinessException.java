package com.lyq.bookManageSystem.common.exception;

import com.lyq.bookManageSystem.common.enums.BusinessErrorCode;

public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;
    private final int httpCode;

    // 构造方法  允许不使用枚举类
    public BusinessException(int code, String message, int httpCode) {
        super(message); // 继承父类消息
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    //构造方法
    public BusinessException(BusinessErrorCode usernameExist) {
        this.code = usernameExist.getCode();
        this.message = usernameExist.getMessage();
        this.httpCode = usernameExist.getHttpCode();
    }


    // Getter
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getHttpCode() {
        return httpCode;
    }
}


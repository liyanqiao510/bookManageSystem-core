package com.lyq.bookManageSystem.common.exception;

import com.lyq.bookManageSystem.common.enums.BusinessErrorCode;

public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    // 构造方法
    public BusinessException(int code, String message) {
        super(message); // 继承父类消息
        this.code = code;
        this.message = message;
    }

    public BusinessException(BusinessErrorCode usernameExist) {
        this.code = usernameExist.getCode();
        this.message = usernameExist.getMessage();
    }


    // Getter
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


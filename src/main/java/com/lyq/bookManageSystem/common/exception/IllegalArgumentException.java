package com.lyq.bookManageSystem.common.exception;

public class IllegalArgumentException extends RuntimeException {
    private final int code;
    private final String message;

    // 构造方法
    public IllegalArgumentException(int code, String message) {
        super(message); // 继承父类消息
        this.code = code;
        this.message = message;
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


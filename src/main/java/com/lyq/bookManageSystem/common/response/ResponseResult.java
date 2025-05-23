package com.lyq.bookManageSystem.common.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 返回结果集
 *
 * @author javadog
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("统一结果集处理器")
public class ResponseResult<T> {

    @ApiModelProperty(value = "状态码")
    private Integer code;


    @ApiModelProperty(value = "返回信息")
    private String message;


    @ApiModelProperty(value = "数据")
    private T data;

    private static <T> ResponseResult<T> response( Integer code, String message,  T data) {

        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        responseResult.setData(data);
        return responseResult;
    }

    private static <T> ResponseResult<T> response( Integer code, String message) {

        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }

    private static <T> ResponseResult<T> response(Integer code ) {

        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        return responseResult;
    }


    //成功信息
    public static <T> ResponseResult<T> success( String message, T data) {

        return response(20000,   message, data);
    }

    public static <T> ResponseResult<T> success( String message) {

        return response(20000,   message);
    }

    public static <T> ResponseResult<T> success() {

        return response(20000);
    }

    //报错信息
    public static <T> ResponseResult<T> error(  String message,  Integer code, T data  ) {

        return response(code , message, data);
    }

    public static <T> ResponseResult<T> error(  String message,  Integer code) {

        return response(code , message);
    }

}

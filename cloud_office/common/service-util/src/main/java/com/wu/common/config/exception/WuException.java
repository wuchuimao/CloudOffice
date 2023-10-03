package com.wu.common.config.exception;

import com.wu.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 20:27
 * @ Description：
 */
@Data
public class WuException extends RuntimeException {

    private Integer code;
    private String message;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param code
     * @param message
     */
    public WuException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public WuException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "WuException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }

}

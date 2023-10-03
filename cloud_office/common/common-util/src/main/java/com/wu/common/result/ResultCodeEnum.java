package com.wu.common.result;

import lombok.Getter;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 16:46
 * @ Description：
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    UPLOAD_FAIL(202, "上传失败"),
    SERVICE_ERROR(2012, "服务异常"),
    PHONE_LOGIN_ERROR(203,"手机号码不存在，绑定失败"),
    DATA_ERROR(204, "数据异常"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    LOGIN_ERROR(206, "认证失败")
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

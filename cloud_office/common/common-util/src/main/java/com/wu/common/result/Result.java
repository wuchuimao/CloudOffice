package com.wu.common.result;

import lombok.Data;

/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 16:48
 * @ Description：
 */
@Data
public class Result<T> {
    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private T data;

    public Result(){}

    // 返回数据
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null)
            result.setData(data);
        return result;
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }


    /**
     * 操作成功
     * @param data  baseCategory1List
     * @param <T>
     * @return
     */
    public static<T> Result<T> ok(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> ok(){
        return build(null, ResultCodeEnum.SUCCESS);
    }


    /**
     * 操作失败
     */

//    public static<T> Result<T> fail(T data){
////        Result<T> result = build(data);
//        return build(data, ResultCodeEnum.FAIL);
//    }

    public static<T> Result<T> fail(){
        return build(null, ResultCodeEnum.FAIL);
    }

    public static<T> Result<T> fail(ResultCodeEnum resultCodeEnum){
        return build(null, resultCodeEnum);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}

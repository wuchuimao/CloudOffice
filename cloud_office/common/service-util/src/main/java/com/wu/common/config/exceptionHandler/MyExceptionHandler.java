package com.wu.common.config.exceptionHandler;

import com.wu.common.config.exception.WuException;
import com.wu.common.result.Result;
import com.wu.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @ Author     ：ChuiMao Wu
 * @ create     : 2023-09-05 20:18
 * @ Description：基于注解的异常处理器类
 */
//表示当前类是一个处理异常的类
@ControllerAdvice
public class MyExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage());
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage()+"\n"+"执行了特定异常处理-数学运算异常");
    }

    //自定义异常处理
    @ExceptionHandler(WuException.class)
    @ResponseBody
    public Result error(WuException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.build(null, ResultCodeEnum.PERMISSION);
    }
}

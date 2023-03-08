package com.github.douyayun.encrypt.annotation;

import com.github.douyayun.encrypt.exception.SecurityBodyException;

import java.lang.annotation.*;

/**
 * 报文解密
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptBody {

    /**
     * 请求参数加密
     */
    boolean required() default false;

    /**
     * 请求数据时间戳校验时间差
     * 超过(当前时间-指定时间)的数据认定为伪造
     * 注意应用程序需要捕获 {@link SecurityBodyException} 异常
     */
    long timeout() default 3000;
}

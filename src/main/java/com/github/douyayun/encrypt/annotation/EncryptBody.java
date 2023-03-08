package com.github.douyayun.encrypt.annotation;

import java.lang.annotation.*;

/**
 * 报文加密
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptBody {

}

package com.github.douyayun.encrypt.annotation;

import com.github.douyayun.encrypt.advice.EncryptRequestBodyAdvice;
import com.github.douyayun.encrypt.advice.EncryptResponseBodyAdvice;
import com.github.douyayun.encrypt.config.SecurityBodyProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启报文加密与解密
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({SecurityBodyProperties.class, EncryptResponseBodyAdvice.class, EncryptRequestBodyAdvice.class})
public @interface EnableSecurityBody {

}

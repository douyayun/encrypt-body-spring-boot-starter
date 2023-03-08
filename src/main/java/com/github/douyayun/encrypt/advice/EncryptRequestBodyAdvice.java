package com.github.douyayun.encrypt.advice;

import com.github.douyayun.encrypt.annotation.DecryptBody;
import com.github.douyayun.encrypt.config.SecurityBodyProperties;
import com.github.douyayun.encrypt.exception.SecurityBodyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 请求加密配置
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
@ControllerAdvice
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean encrypt;
    private DecryptBody decryptBodyAnnotation;

    @Autowired
    private SecurityBodyProperties securityBodyProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();
        if (Objects.isNull(method)) {
            encrypt = false;
            return false;
        }
        if (method.isAnnotationPresent(DecryptBody.class) && securityBodyProperties.isOpen()) {
            encrypt = true;
            decryptBodyAnnotation = methodParameter.getMethodAnnotation(DecryptBody.class);
            return true;
        }
        // 此处如果按照原逻辑直接返回encrypt, 会造成一次修改为true之后, 后续请求都会变成true, 在不支持时, 需要做修正
        encrypt = false;
        return false;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (encrypt) {
            try {
                return new DecryptHttpInputMessage(inputMessage, securityBodyProperties, decryptBodyAnnotation);
            } catch (SecurityBodyException e) {
                throw e;
            } catch (Exception e) {
                log.error("Decryption failed", e);
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}

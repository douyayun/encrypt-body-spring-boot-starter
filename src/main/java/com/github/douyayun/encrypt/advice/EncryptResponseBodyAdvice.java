package com.github.douyayun.encrypt.advice;

import com.github.douyayun.encrypt.annotation.EncryptBody;
import com.github.douyayun.encrypt.config.SecurityBodyProperties;
import com.github.douyayun.encrypt.util.Base64Util;
import com.github.douyayun.encrypt.util.JsonUtils;
import com.github.douyayun.encrypt.util.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 请求加密配置
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private boolean encrypt;
    @Autowired
    private SecurityBodyProperties securityBodyProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        if (Objects.isNull(method)) {
            return encrypt;
        }
        encrypt = method.isAnnotationPresent(EncryptBody.class) && securityBodyProperties.isOpen();
        return encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // EncryptResponseBodyAdvice.setEncryptStatus(false);
        // Dynamic Settings Not Encrypted
        Boolean status = encryptLocal.get();
        if (null != status && !status) {
            encryptLocal.remove();
            return body;
        }
        if (encrypt) {
            String publicKey = securityBodyProperties.getPublicKey();
            try {
                String content = JsonUtils.writeValueAsString(body);
                if (!StringUtils.hasText(publicKey)) {
                    throw new NullPointerException("Please configure rsa.encrypt.privatekeyc parameter!");
                }
                byte[] data = content.getBytes();
                byte[] encodedData = RSAUtil.encrypt(data, publicKey);
                String result = Base64Util.encode(encodedData);
                if (securityBodyProperties.isShowLog()) {
                    log.info("Pre-encrypted data：{}，After encryption：{}", content, result);
                }
                return result;
            } catch (Exception e) {
                log.error("Encrypted data exception", e);
            }
        }

        return body;
    }
}

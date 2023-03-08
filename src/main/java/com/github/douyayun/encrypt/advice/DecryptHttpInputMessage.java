package com.github.douyayun.encrypt.advice;

import com.github.douyayun.encrypt.annotation.DecryptBody;
import com.github.douyayun.encrypt.config.SecurityBodyProperties;
import com.github.douyayun.encrypt.exception.SecurityBodyException;
import com.github.douyayun.encrypt.util.Base64Util;
import com.github.douyayun.encrypt.util.JsonUtils;
import com.github.douyayun.encrypt.util.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 请求加密配置
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
public class DecryptHttpInputMessage implements HttpInputMessage {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private HttpHeaders headers;
    private InputStream body;


    public DecryptHttpInputMessage(HttpInputMessage inputMessage, SecurityBodyProperties securityBodyProperties, DecryptBody decrypt) throws Exception {

        String privateKey = securityBodyProperties.getPrivateKey();
        String charset = securityBodyProperties.getCharset();
        boolean showLog = securityBodyProperties.isShowLog();
        boolean timestampCheck = securityBodyProperties.isTimestampCheck();

        if (StringUtils.isEmpty(privateKey)) {
            throw new IllegalArgumentException("privateKey is null");
        }

        this.headers = inputMessage.getHeaders();
        String content = new BufferedReader(new InputStreamReader(inputMessage.getBody()))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        String decryptBody;
        // 未加密内容
        if (content.startsWith("{")) {
            // 必须加密
            if (decrypt.required()) {
                log.error("not support unencrypted content:{}", content);
                throw new SecurityBodyException("not support unencrypted content");
            }
            log.info("Unencrypted without decryption:{}", content);
            decryptBody = content;
        } else {
            StringBuilder json = new StringBuilder();
            content = content.replaceAll(" ", "+");

            if (!StringUtils.isEmpty(content)) {
                String[] contents = content.split("\\|");
                for (String value : contents) {
                    value = new String(RSAUtil.decrypt(Base64Util.decode(value), privateKey), charset);
                    json.append(value);
                }
            }
            decryptBody = json.toString();
            if (showLog) {
                log.info("Encrypted data received：{},After decryption：{}", content, decryptBody);
            }
        }

        // 开启时间戳检查
        if (timestampCheck) {
            // 容忍最小请求时间
            long toleranceTime = System.currentTimeMillis() - decrypt.timeout();
            long requestTime = JsonUtils.getNode(decryptBody, "timestamp").asLong();
            // 如果请求时间小于最小容忍请求时间, 判定为超时
            if (requestTime < toleranceTime) {
                log.error("Encryption request has timed out, toleranceTime:{}, requestTime:{}, After decryption：{}", toleranceTime, requestTime, decryptBody);
                throw new SecurityBodyException("request timeout");
            }
        }

        this.body = new ByteArrayInputStream(decryptBody.getBytes());
    }

    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}

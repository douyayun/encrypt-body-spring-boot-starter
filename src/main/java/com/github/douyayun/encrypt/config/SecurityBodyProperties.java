package com.github.douyayun.encrypt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 报文加密与解密配置
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
@ConfigurationProperties(prefix = "security.body")
@Configuration
@Data
public class SecurityBodyProperties {

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 应用公钥
     */
    private String publicKey;

    /**
     * 加密字符集
     */
    private String charset = "UTF-8";

    /**
     *
     */
    private boolean open = true;

    /**
     * 打开日志
     */
    private boolean showLog = false;

    /**
     * 请求时间戳校验
     */
    private boolean timestampCheck = false;

}

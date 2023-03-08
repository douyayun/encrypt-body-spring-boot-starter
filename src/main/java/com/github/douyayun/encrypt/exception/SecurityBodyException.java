package com.github.douyayun.encrypt.exception;


/**
 * 报文加密与解密异常
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
public class SecurityBodyException extends RuntimeException {

    public SecurityBodyException(String msg) {
        super(msg);
    }
}

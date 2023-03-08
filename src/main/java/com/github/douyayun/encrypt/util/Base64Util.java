package com.github.douyayun.encrypt.util;

import org.apache.commons.codec.binary.Base64;

/**
 * 工具类
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
public class Base64Util {

    /**
     * Decoding to binary
     *
     * @param base64 base64
     * @return byte
     */
    public static byte[] decode(String base64) {
        return Base64.decodeBase64(base64);
    }

    /**
     * Binary encoding as a string
     *
     * @param bytes byte
     * @return String
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
}

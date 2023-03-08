package com.github.douyayun.encrypt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json工具类
 *
 * @author: houp
 * @date: 2023/3/8 20:57
 * @version: 1.0.0
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static JsonNode getNode(String content, String key) throws IOException {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(content);
        return jsonNode.get(key);
    }

    public static String writeValueAsString(Object body) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(body);
    }
}

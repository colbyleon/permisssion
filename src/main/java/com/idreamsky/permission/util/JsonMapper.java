package com.idreamsky.permission.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: colby
 * @Date: 2018/12/16 11:21
 */
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // config
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false))
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .findAndRegisterModules();
    }

    public static <T> String toJSONString(T src) {
        if (src == null) {
            return null;
        }
        try {
            return src instanceof String ? (String) src
                    : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("parse object to String exception, error{}", e);
        }
        return null;
    }

    public static <T> T parseObject(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T) src
                    : objectMapper.readValue(src, typeReference);
        } catch (Exception e) {
            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, e:{}", src, typeReference.getType(), e);
        }
        return null;
    }
}

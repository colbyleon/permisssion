package com.idreamsky.permission.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * @Author: colby
 * @Date: 2018/12/16 11:21
 */
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // config
        objectMapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public static <T> String toJSONString(T src) {
        if (src == null) {
            return null;
        }
        try{
            return src instanceof String ? (String) src
                    : objectMapper.writeValueAsString(src);
        }catch(Exception e){
            log.warn("parse object to String exception, error{}",e);
        }
        return null;
    }

    public static <T> T parseObject(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }
        try{
            return typeReference.getType().equals(String.class) ? (T)src
                    : objectMapper.readValue(src, typeReference);
        }catch(Exception e){
            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}",src,typeReference.getType());
        }
        return null;
    }
}

package com.idreamsky.permission.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: colby
 * @Date: 2018/12/15 23:01
 */
@Getter
@Setter
@Accessors(chain = true)
public class JsonData {

    private boolean ret;

    private String msg;

    private Object data;

    private JsonData(boolean ret) {
        this.ret = ret;
    }

    public static JsonData success(Object data, String msg) {
        return new JsonData(true)
                .setData(data)
                .setMsg(msg);
    }

    public static JsonData success(Object data) {
        return new JsonData(true)
                .setData(data);
    }

    public static JsonData success() {
        return new JsonData(true);
    }

    public static JsonData fail(String msg) {
        return new JsonData(false)
                .setMsg(msg);
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>(3);
        result.put("ret", ret);
        result.put("msg",msg);
        result.put("data",data);
        return result;
    }
}

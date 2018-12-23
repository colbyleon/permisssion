package com.idreamsky.permission.common;

import com.idreamsky.permission.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: colby
 * @Date: 2018/12/22 15:35
 */
public class RequestHolder {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHoler = new ThreadLocal<>();

    public static void add(User user) {
        userHolder.set(user);
    }

    public static void add(HttpServletRequest request) {
        requestHoler.set(request);
    }

    public static User getCurrentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest(){
        return requestHoler.get();
    }

    public static void remove(){
        userHolder.remove();
        requestHoler.remove();
    }
}

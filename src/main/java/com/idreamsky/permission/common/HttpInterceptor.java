package com.idreamsky.permission.common;

import com.idreamsky.permission.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: colby
 * @Date: 2018/12/16 12:06
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("request url:{}, params:{}", url, JsonMapper.toJSONString(parameterMap));
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url = request.getRequestURI();
        long startTime = (long) request.getAttribute(START_TIME);
        log.info("request finished. url:{}, cost:{}", url, System.currentTimeMillis() - startTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            String url = request.getRequestURI();
            Map<String, String[]> parameterMap = request.getParameterMap();
            long startTime = (long) request.getAttribute(START_TIME);
            log.info("request exception. url:{}, cost:{}, params:{}", url, System.currentTimeMillis() - startTime
                    , JsonMapper.toJSONString(parameterMap));
        }
    }
}

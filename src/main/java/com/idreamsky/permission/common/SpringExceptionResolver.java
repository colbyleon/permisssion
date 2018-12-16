package com.idreamsky.permission.common;

import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: colby
 * @Date: 2018/12/15 23:07
 */
@Component
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURI();
        ModelAndView mv;
        String defaultMsg = "System error";

        // .json, .page
        // 这里我们要求项目中所有请求json的不要使用.jsp|html|css|js|png|jpg|jpeg|gif结尾
        String pageUrl = "^.*\\.(?:jsp|html|css|js|png|jpg|jpeg|gif)$";
        if (!url.matches(pageUrl)) {
            JsonData result;
            if (ex instanceof PermissionException
                    || ex instanceof ParamException) {
                result = JsonData.fail(ex.getMessage());
            } else {
                log.error("unknown json exception,url:{}", url, ex);
                result = JsonData.fail(defaultMsg);
            }
            mv = new ModelAndView("jsonView", result.toMap());
        } else {
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        }
        return mv;
    }
}

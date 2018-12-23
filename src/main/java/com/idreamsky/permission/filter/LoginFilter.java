package com.idreamsky.permission.filter;

import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: colby
 * @Date: 2018/12/22 15:43
 */
@Slf4j
@Component
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            // user == null 说明没有登陆
            resp.sendRedirect("/signin.html");
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(req);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}

package com.idreamsky.permission.controller;

import com.idreamsky.permission.model.User;
import com.idreamsky.permission.service.UserService;
import com.idreamsky.permission.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: colby
 * @Date: 2018/12/20 22:27
 */
@Controller
public class LoginController {
    @Resource
    private UserService userService;

    @RequestMapping("/signin.html")
    public String signin() {
        return "signin";
    }

    @RequestMapping("/login.html")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.findByKeyword(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不可以为空";
        } else if (user == null) {
            errorMsg = "查询不到指定的用户";
        } else if (!user.getPassword().equals(EncryptUtil.md5HexString(password))) {
            errorMsg = "用户名或密码错误";
        } else if (user.getStatus() != 1) {
            errorMsg = "用户已被冻结，请联系管理员";
        } else {
            // login success
            request.getSession().setAttribute("user", user);
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index.html");
            }
            return;
        }

        request.setAttribute("errorMsg",errorMsg);
        request.setAttribute("username",username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret",ret);
        }
        String path = "signin.html";
        request.getRequestDispatcher(path).forward(request,response);
    }

    @RequestMapping("/logout.html")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        return "redirect:signin.html";
    }
}

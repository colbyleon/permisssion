package com.idreamsky.permission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: colby
 * @Date: 2018/12/20 22:50
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index.html")
    public ModelAndView index(){
        return new ModelAndView("admin");
    }
}

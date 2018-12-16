package com.idreamsky.permission.controller;

import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.exception.PermissionException;
import com.idreamsky.permission.param.TestVo;
import com.idreamsky.permission.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author: colby
 * @Date: 2018/12/15 21:35
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello")
    @ResponseBody
    public JsonData hello(){
        log.info("hello");
        return JsonData.success("hello, permission");
    }

    @RequestMapping("/exception")
    @ResponseBody
    public JsonData testException(){
        throw new RuntimeException("test exception");

    }

    @RequestMapping("/validate")
    @ResponseBody
    public JsonData validate(TestVo vo) throws ParamException {
        log.info("validate");
        BeanValidator.check(vo);
        return JsonData.success("test validate");
    }
}

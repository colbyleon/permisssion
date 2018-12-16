package com.idreamsky.permission.controller;


import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.param.DeptParam;
import com.idreamsky.permission.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class DeptController {

    @Resource
    private DeptService deptService;

    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveDept(DeptParam deptParam) {
        deptService.save(deptParam);
        return JsonData.success();
    }
}

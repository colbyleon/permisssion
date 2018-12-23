package com.idreamsky.permission.controller;


import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.param.DeptParam;
import com.idreamsky.permission.service.DeptService;
import com.idreamsky.permission.service.TreeService;
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

    @Resource
    private TreeService treeService;

    @RequestMapping("/dept.html")
    public String page(){
        return "dept";
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveDept(DeptParam param) {
        deptService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/tree")
    @ResponseBody
    public JsonData tree(){
        return JsonData.success(treeService.deptTree());
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateDept(DeptParam param) {
        deptService.update(param);
        return JsonData.success();
    }
}

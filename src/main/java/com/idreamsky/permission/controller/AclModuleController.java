package com.idreamsky.permission.controller;


import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.param.AclModuleParam;
import com.idreamsky.permission.param.DeptParam;
import com.idreamsky.permission.service.AclModuleService;
import com.idreamsky.permission.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 * 权限表、权限模块 前端控制器
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class AclModuleController {
    @Resource
    private AclModuleService aclModuleService;

    @Resource
    private TreeService treeService;

    @RequestMapping("/acl.html")
    public String page(){
        return "acl";
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam param) {
        aclModuleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param) {
        aclModuleService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/tree")
    @ResponseBody
    public JsonData tree(){
        return JsonData.success(treeService.aclModuleTree());
    }

    @RequestMapping("/delete")
    @ResponseBody
    public JsonData deleteAclModule(@RequestParam("id") int id){
        aclModuleService.delete(id);
        return JsonData.success();
    }
}

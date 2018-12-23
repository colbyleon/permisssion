package com.idreamsky.permission.controller;


import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.beans.PageResult;
import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.model.Acl;
import com.idreamsky.permission.param.AclModuleParam;
import com.idreamsky.permission.param.AclParam;
import com.idreamsky.permission.service.AclService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@RestController
@RequestMapping("/sys/acl")
public class AclController {
    @Resource
    private AclService aclService;


    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveAcl(AclParam param) {
        aclService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateAcl(AclParam param) {
        aclService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return JsonData.success(aclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }
}

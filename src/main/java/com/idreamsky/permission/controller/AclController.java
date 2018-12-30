package com.idreamsky.permission.controller;


import com.google.common.collect.Maps;
import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.model.Role;
import com.idreamsky.permission.param.AclParam;
import com.idreamsky.permission.service.AclService;
import com.idreamsky.permission.service.RoleService;
import com.idreamsky.permission.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

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

    @RequestMapping("/acls")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") Integer aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<Role> roleList = roleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", roleService.getUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}

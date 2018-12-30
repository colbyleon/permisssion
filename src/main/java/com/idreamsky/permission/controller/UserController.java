package com.idreamsky.permission.controller;


import com.google.common.collect.Maps;
import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.beans.PageResult;
import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.UserParam;
import com.idreamsky.permission.service.RoleService;
import com.idreamsky.permission.service.TreeService;
import com.idreamsky.permission.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Controller
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private TreeService treeService;
    @Resource
    private RoleService roleService;

    @RequestMapping("/noAuth.html")
    public String noAuth(){
        return "noAuth";
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveDept(UserParam param) {
        userService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateDept(UserParam param) {
        userService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonData list(@RequestParam("deptId") Integer deptId, PageQuery pageQuery) {
        PageResult<User> result = userService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }

    @RequestMapping("/acls")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") Integer userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", treeService.userAclTree(userId));
        map.put("roles", roleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}

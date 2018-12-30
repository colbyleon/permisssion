package com.idreamsky.permission.controller;


import com.google.common.collect.Maps;
import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.RoleParam;
import com.idreamsky.permission.service.*;
import com.idreamsky.permission.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private TreeService treeService;
    @Resource
    private RoleAclService roleAclService;
    @Resource
    private RoleUserService roleUserService;
    @Resource
    private UserService userService;

    @RequestMapping("/role.html")
    public String page() {
        return "role";
    }


    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveRole(RoleParam param) {
        roleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateRole(RoleParam param) {
        roleService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/list")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(roleService.getAll());
    }

    @RequestMapping("/roleTree")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success(treeService.roleTree(roleId));
    }

    @RequestMapping("/changeAcls")
    @ResponseBody
    public JsonData changAcls(
            @RequestParam("roleId") Integer roleId,
            @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        roleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }

    @RequestMapping("/changeUsers")
    @ResponseBody
    public JsonData changeUsers(
            @RequestParam("roleId") Integer roleId,
            @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        roleUserService.changeRoleUsers(roleId, userIdList);
        return JsonData.success();
    }

    @RequestMapping("/users")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId) {
        List<User> selectedUserList = roleUserService.getListByRoleId(roleId);
        List<User> allUserList = userService.getAll();
        // 利用hash加速
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(User::getId).collect(Collectors.toSet());
        List<User> unselectedUserList = allUserList.stream()
                .filter(user -> user.getStatus() == 1)
                .filter(user -> !selectedUserIdSet.contains(user.getId())).collect(Collectors.toList());
        Map<String, List<User>> map = Maps.newLinkedHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);
        return JsonData.success(map);
    }
}

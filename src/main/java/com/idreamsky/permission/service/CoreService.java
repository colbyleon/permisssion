package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.AclMapper;
import com.idreamsky.permission.dao.RoleAclMapper;
import com.idreamsky.permission.dao.RoleUserMapper;
import com.idreamsky.permission.model.Acl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: colby
 * @Date: 2018/12/23 12:13
 */
@Service
public class CoreService {

    @Resource
    private AclMapper aclMapper;

    @Resource
    private RoleUserMapper roleUserMapper;

    @Resource
    private RoleAclMapper roleAclMapper;

    public List<Acl> getCurrentUserAclList() {
        Integer userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    public List<Acl> getRoleAclList(int roleId) {
        List<Integer> roleAclIdList = roleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(roleAclIdList)) {
            return Lists.newArrayList();
        }
        return aclMapper.selectBatchIds(roleAclIdList);
    }

    private List<Acl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            return aclMapper.selectList(Wrappers.emptyWrapper());
        }
        List<Integer> userRoleIdList = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = roleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return aclMapper.selectBatchIds(userAclIdList);
    }

    private boolean isSuperAdmin() {
        return true;
    }
}

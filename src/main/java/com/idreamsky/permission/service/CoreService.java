package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.idreamsky.permission.beans.CacheKeyConstants;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.AclMapper;
import com.idreamsky.permission.dao.RoleAclMapper;
import com.idreamsky.permission.dao.RoleUserMapper;
import com.idreamsky.permission.model.Acl;
import com.idreamsky.permission.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Resource
    private CacheService cacheService;

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

    public List<Acl> getUserAclList(int userId) {
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

    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<Acl> aclList = aclMapper.selectList(Wrappers.<Acl>query().eq("url", url));
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }
        // 规则: 只要有一个权限点有权限就认是有权限的
        boolean hasValidAcl = false;
        List<Acl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(Acl::getId).collect(Collectors.toSet());
        for (Acl acl : aclList) {
            // 无效的权限点跳过，所有权限点都无效时不成有权限
            if (acl.getStatus() != 1) {
                continue;
            }
            hasValidAcl = true;
            // 判断一个用户是否有某个权限点的访问权限
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        return !hasValidAcl;
    }

    private List<Acl> getCurrentUserAclListFromCache() {
        Integer userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = cacheService.getFromCache(CacheKeyConstants.USER_ACLS, userId.toString());
        if (StringUtils.isBlank(cacheValue)) {
            List<Acl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                cacheService.saveCache(JsonMapper.toJSONString(aclList), 600, CacheKeyConstants.USER_ACLS, userId.toString());
            }
            return aclList;
        }
        return JsonMapper.parseObject(cacheValue, new TypeReference<List<Acl>>() {
        });
    }

    private boolean isSuperAdmin() {
        return false;
//        String username = RequestHolder.getCurrentUser().getUsername();
//        return "admin".equalsIgnoreCase(username);
    }
}

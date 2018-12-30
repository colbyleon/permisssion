package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.idreamsky.permission.beans.LogType;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.LogMapper;
import com.idreamsky.permission.dao.RoleAclMapper;
import com.idreamsky.permission.model.LogWithBlobs;
import com.idreamsky.permission.model.RoleAcl;
import com.idreamsky.permission.util.IpUtil;
import com.idreamsky.permission.util.JsonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限相关表 服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class RoleAclService {
    @Resource
    private RoleAclMapper roleAclMapper;
    @Resource
    private LogMapper logMapper;

    @Transactional(rollbackFor = Exception.class)
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = roleAclMapper.getAclIdListByRoleIdList(Collections.singletonList(roleId));
        if (originAclIdList.equals(aclIdList)) {
            return;
        }
        updateRoleAcls(roleId, aclIdList);
        saveRoleAclLog(roleId,originAclIdList,aclIdList);
    }

    public void updateRoleAcls(Integer roleId, List<Integer> aclIdList) {
        UpdateWrapper<RoleAcl> deleteByRoleId = Wrappers.<RoleAcl>update().eq("role_id", roleId);
        roleAclMapper.delete(deleteByRoleId);

        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }

        List<RoleAcl> roleAcls = aclIdList.stream()
                .map(aclId -> new RoleAcl(roleId, aclId))
                .peek(roleAcl -> {
                    roleAcl.setOperateTime(LocalDateTime.now());
                    roleAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    roleAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
                })
                .collect(Collectors.toList());
        roleAclMapper.batchInsert(roleAcls);
    }

    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        LogWithBlobs log = new LogWithBlobs();
        log.setType(LogType.TYPE_ROLE_ACL);
        log.setTargetId(roleId);
        log.setOldValue(before == null ? "" : JsonMapper.toJSONString(before));
        log.setNewValue(after == null ? "" : JsonMapper.toJSONString(after));
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(LocalDateTime.now());
        log.setStatus(1);
        logMapper.insert(log);
    }
}

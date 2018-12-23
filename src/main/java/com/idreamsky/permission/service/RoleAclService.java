package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.RoleAclMapper;
import com.idreamsky.permission.model.RoleAcl;
import com.idreamsky.permission.util.IpUtil;
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

    public void changRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = roleAclMapper.getAclIdListByRoleIdList(Collections.singletonList(roleId));
        if (originAclIdList.equals(aclIdList)) {
            return;
        }
        updateRoleAcls(roleId, aclIdList);
    }

    @Transactional(rollbackFor = Exception.class)
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
}

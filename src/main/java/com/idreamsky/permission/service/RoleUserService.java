package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.UserMapper;
import com.idreamsky.permission.dao.RoleUserMapper;
import com.idreamsky.permission.model.RoleUser;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.util.IpUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色用户相关表 服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class RoleUserService{

    @Resource
    private RoleUserMapper roleUserMapper;
    @Resource
    private UserMapper userMapper;

    public List<User> getListByRoleId(int roleId) {
        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return userMapper.selectBatchIds(userIdList);
    }

    public void changRoleUsers(Integer roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = roleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.equals(userIdList)) {
            return;
        }
        updateRoleUsers(roleId, userIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUsers(Integer roleId, List<Integer> userIdList) {
        UpdateWrapper<RoleUser> deleteByRoleId = Wrappers.<RoleUser>update().eq("role_id", roleId);
        roleUserMapper.delete(deleteByRoleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        List<RoleUser> roleUsers = userIdList.stream()
                .map(userId -> new RoleUser(roleId, userId))
                .peek(roleUser -> {
                    roleUser.setOperateTime(LocalDateTime.now());
                    roleUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                    roleUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                })
                .collect(Collectors.toList());
        roleUserMapper.batchInsert(roleUsers);
    }
}

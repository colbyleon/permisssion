package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.RoleAclMapper;
import com.idreamsky.permission.dao.RoleUserMapper;
import com.idreamsky.permission.dao.UserMapper;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.Role;
import com.idreamsky.permission.dao.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.RoleParam;
import com.idreamsky.permission.util.BeanValidator;
import com.idreamsky.permission.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleUserMapper roleUserMapper;
    @Resource
    private RoleAclMapper roleAclMapper;
    @Resource
    private UserMapper userMapper;

    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        Role role = Role.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();

        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateTime(LocalDateTime.now());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        roleMapper.insert(role);
    }

    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        Role before = roleMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        Role role = Role.builder().id(param.getId()).name(param.getName()).status(param.getStatus())
                .type(param.getType()).remark(param.getRemark()).build();

        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateTime(LocalDateTime.now());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        roleMapper.updateById(role);
    }

    public List<Role> getAll(){
        return roleMapper.selectList(Wrappers.emptyWrapper());
    }

    public List<Role> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return roleMapper.selectBatchIds(roleIdList);
    }

    public List<Role> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = roleAclMapper.getRoleIdListByAclId(aclId);
        return roleMapper.selectBatchIds(roleIdList);
    }

    public List<User> getUserListByRoleList(List<Role> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(Role::getId).collect(Collectors.toList());
        List<Integer> userIdList = roleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return userMapper.selectBatchIds(userIdList);
    }

    private boolean checkExist(String name,Integer id){
        return roleMapper.selectCount(Wrappers.<Role>query().eq("name", name).ne("id", id)) > 0;
    }

}

package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idreamsky.permission.model.RoleAcl;
import com.idreamsky.permission.dao.RoleAclMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限相关表 服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class RoleAclServiceImpl extends ServiceImpl<RoleAclMapper, RoleAcl> implements IService<RoleAcl> {

}

package com.idreamsky.permission.dao;

import com.idreamsky.permission.model.RoleAcl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色权限相关表 Mapper 接口
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
public interface RoleAclMapper extends BaseMapper<RoleAcl> {
    /**
     * 根据角色id列表获取权限id列表
     * @param roleIdList  roleIdList
     * @return RoleAclIdList
     */
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    /**
     * 批量插入角色权限关系
     * @param roleAclList 角色权限关系
     */
    void batchInsert(@Param("roleAclList") List<RoleAcl> roleAclList);

    /**
     * aclId -> roleIds
     * @param aclId 权限点id
     * @return 角色ids
     */
    List<Integer> getRoleIdListByAclId(int aclId);
}

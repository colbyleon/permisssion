package com.idreamsky.permission.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.idreamsky.permission.model.RoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色用户相关表 Mapper 接口
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
public interface RoleUserMapper extends BaseMapper<RoleUser> {
    /**
     * 根据用户id查询其所有角色id
     *
     * @param userId 用户id
     * @return 角色idList
     */
    List<Integer> getRoleIdListByUserId(Integer userId);

    /**
     * roleId -> userIds
     *
     * @param roleId 角色id
     * @return 用户ids
     */
    List<Integer> getUserIdListByRoleId(int roleId);

    /**
     * 根据角色id列表获取用户id列表
     * @param roleId  角色id
     * @return userIds
     */
    List<Integer> getUserIdListByRoleIdList(Integer roleId);

    /**
     * 批量插入角色用户关系
     * @param roleUsers 角色用户关系
     */
    void batchInsert(@Param("roleUsers") List<RoleUser> roleUsers);
}

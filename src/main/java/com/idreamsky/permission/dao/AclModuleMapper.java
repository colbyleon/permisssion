package com.idreamsky.permission.dao;

import com.idreamsky.permission.model.AclModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表、权限模块 Mapper 接口
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
public interface AclModuleMapper extends BaseMapper<AclModule> {
    /**
     * 批量更新
     * @param subAclModuleList list
     */
    void batchUpdateLevel(@Param("subAclModuleList") List<AclModule> subAclModuleList);
}

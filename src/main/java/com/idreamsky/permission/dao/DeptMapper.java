package com.idreamsky.permission.dao;

import com.idreamsky.permission.model.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
public interface DeptMapper extends BaseMapper<Dept> {
    /**
     * 批量更新dept
     * @param deptList 部门list
     */
    void batchUpdateLevel(@Param("deptList") List<Dept> deptList);
}

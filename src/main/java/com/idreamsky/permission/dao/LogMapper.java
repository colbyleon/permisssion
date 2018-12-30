package com.idreamsky.permission.dao;

import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.dto.SearchLogDto;
import com.idreamsky.permission.model.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.idreamsky.permission.model.LogWithBlobs;
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
public interface LogMapper extends BaseMapper<LogWithBlobs> {
    /**
     * 分面查询前置
     * @param dto dto
     * @return 数量
     */
    int countBySearchDto(@Param("dto") SearchLogDto dto);

    /**
     * 分页查询log
     * @param dto dto
     * @param page page
     * @return
     */
    List<LogWithBlobs> getPageListBySearchDto(
            @Param("dto") SearchLogDto dto,
            @Param("page")PageQuery page);
}

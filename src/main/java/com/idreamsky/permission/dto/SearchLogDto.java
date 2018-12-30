package com.idreamsky.permission.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: colby
 * @Date: 2018/12/30 17:28
 */
@Data
public class SearchLogDto {
    /**
     * @see com.idreamsky.permission.beans.LogType
     */
    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;
    /**
     * 开始时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime fromTime;

    private LocalDateTime toTime;
}

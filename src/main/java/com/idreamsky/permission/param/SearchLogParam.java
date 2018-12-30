package com.idreamsky.permission.param;

import lombok.Data;

/**
 * @Author: colby
 * @Date: 2018/12/30 17:26
 */
@Data
public class SearchLogParam {

    private Integer type;

    private String beforeSeg;

    private String afterSeg;

    private String operator;
    /**
     * 开始时间 yyyy-MM-dd HH:mm:ss
     */
    private String fromTime;

    private String toTime;

}

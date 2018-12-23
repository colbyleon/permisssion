package com.idreamsky.permission.beans;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author: colby
 * @Date: 2018/12/22 12:06
 */

@Data
public class PageQuery {
    @Min(1)
    private int pageNo = 1;

    @Min(1)
    private int pageSize = 10;

    private int offset;

    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}

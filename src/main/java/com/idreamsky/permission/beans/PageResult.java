package com.idreamsky.permission.beans;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: colby
 * @Date: 2018/12/22 12:07
 */
@Data
@Builder
@AllArgsConstructor
public class PageResult<T> {
    private List<T> data = Lists.newArrayList();
    private int total;
}

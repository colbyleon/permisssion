package com.idreamsky.permission.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: colby
 * @Date: 2018/12/15 22:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
public class LogWithBlobs extends Log {
    /**
     * 旧值
     */
    private String oldValue;

    /**
     * 新值
     */
    private String newValue;
}

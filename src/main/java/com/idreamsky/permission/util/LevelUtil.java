package com.idreamsky.permission.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: colby
 * @Date: 2018/12/16 15:48
 */
public class LevelUtil {

    public static final String SEPARATOR = ".";

    public static final String ROOT = "0";

    /**
     * 0
     * 0.1
     * 0.1.2
     */
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        }else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}

package com.idreamsky.permission.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: colby
 * @Date: 2018/12/23 17:16
 */
public class StringUtil {

    public static List<Integer> splitToListInt(String str) {
        return Arrays.stream(str.split(","))
                .filter(StringUtils::isNotBlank)
                .map(Integer::valueOf).collect(Collectors.toList());
    }
}

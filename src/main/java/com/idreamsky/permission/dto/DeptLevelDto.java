package com.idreamsky.permission.dto;

import com.google.common.collect.Lists;
import com.idreamsky.permission.model.Dept;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: colby
 * @Date: 2018/12/16 16:12
 */
@Data
public class DeptLevelDto extends Dept {

    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adapt(Dept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept,dto);
        return dto;
    }
}

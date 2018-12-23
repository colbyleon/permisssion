package com.idreamsky.permission.dto;

import com.google.common.collect.Lists;
import com.idreamsky.permission.model.Acl;
import com.idreamsky.permission.model.AclModule;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Author: colby
 * @Date: 2018/12/22 17:44
 */
@Data
public class AclModuleLevelDto extends AclModule {
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclList = Lists.newArrayList();

    public static AclModuleLevelDto adapt(AclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}

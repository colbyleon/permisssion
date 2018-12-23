package com.idreamsky.permission.dto;

import com.idreamsky.permission.model.Acl;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Author: colby
 * @Date: 2018/12/23 12:06
 */
@Data
public class AclDto extends Acl {
    /**
     * 是否要默认选中
     */
    private boolean checked = false;
    /**
     * 是否有权限操作
     */
    private boolean hasAcl = false;

    public static AclDto adapt(Acl acl) {
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(acl, aclDto);
        return aclDto;
    }
}

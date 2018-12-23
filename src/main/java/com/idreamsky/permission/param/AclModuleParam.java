package com.idreamsky.permission.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: colby
 * @Date: 2018/12/22 17:01
 */
@Data
public class AclModuleParam {
    /**
     * 权限模块id
     */
    private Integer id;
    /**
     * 权限模块名称
     */
    @NotBlank
    @Length(min = 2, max = 20)
    private String name;

    /**
     * 上级权限模块id
     */
    private Integer parentId = 0;

    /**
     * 排序字段
     */
    @NotNull
    private Integer seq;

    /**
     * 状态，1：正常，0：冻结
     */
    @Range(min = 0, max = 1)
    @NotNull
    private Integer status;

    /**
     * 备注
     */
    @Length(max = 200)
    private String remark;
}

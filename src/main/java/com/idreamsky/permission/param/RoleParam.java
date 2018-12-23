package com.idreamsky.permission.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: colby
 * @Date: 2018/12/23 10:19
 */
@Data
public class RoleParam {
    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名称
     */
    @NotBlank
    @Length(min = 2, max = 20)
    private String name;

    /**
     * 类型，1：管理员，2：其它
     */
    @Range(min = 1, max = 2)
    private Integer type = 1;

    /**
     * 状态，1：正常，2：冻结
     */
    @NotNull
    @Range(min = 1, max = 2)
    private Integer status;

    /**
     * 备注
     */
    @Length(max = 255)
    private String remark;
}

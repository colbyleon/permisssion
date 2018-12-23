package com.idreamsky.permission.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: colby
 * @Date: 2018/12/22 22:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AclParam {

    /**
     * 权限id
     */
    private Integer id;


    /**
     * 权限名称
     */
    @NotBlank
    @Length(min = 2, max = 64)
    private String name;

    /**
     * 权限模块id
     */
    @NotNull
    private Integer aclModuleId;

    /**
     * 请求的url，可以填正则表达示
     */
    @Length(min = 2, max = 255)
    private String url;

    /**
     * 类型，1：菜单，2：按钮，3：其它
     */
    @NotNull
    @Range(min = 1, max = 3)
    private Integer type;

    /**
     * 状态，1：正常，0：冻结
     */
    @NotNull
    @Range(min = 0,max = 1)
    private Integer status;

    /**
     * 排序
     */
    @NotNull
    private Integer seq;

    /**
     * 备注
     */
    @Length(max = 255)
    private String remark;
}

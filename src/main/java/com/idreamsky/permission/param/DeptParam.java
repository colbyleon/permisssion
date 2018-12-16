package com.idreamsky.permission.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: colby
 * @Date: 2018/12/16 15:39
 */
@Data
public class DeptParam {
    /**
     * 部门id
     */
    private Integer id;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15, min = 2, message = "部门名称长度需要在2-15之间")
    private String name;

    /**
     * 上级部门id
     */
    private Integer parentId;

    /**
     * 排序字段
     */
    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    /**
     * 备注
     */
    @Length(max = 150, message = "备注的长度不能超过150个字")
    private String remark;

}

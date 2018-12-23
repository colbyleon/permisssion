package com.idreamsky.permission.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: colby
 * @Date: 2018/12/19 22:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserParam {
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名
     */
    @NotBlank
    @Length(max = 20, message = "用户名长度需要在20个字以内")
    private String username;

    /**
     * 手机号
     */
    @NotBlank(message = "电话不可以为空")
    @Length(min = 1, max = 13, message = "电话长度在13个字符以内")
    private String telephone;

    /**
     * 邮箱
     */
    @NotBlank
    @Length(min = 5, max = 50)
    private String mail;

    /**
     * 部门id
     */
    @NotNull
    private Integer deptId;

    /**
     * 状态，1：正常 ，0：冻结状态，2：删除
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer status;

    /**
     * 备注
     */
    @Length(max = 200)
    private String remark;

}

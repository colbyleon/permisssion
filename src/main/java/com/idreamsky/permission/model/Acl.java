package com.idreamsky.permission.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false,of = "id")
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_acl")
public class Acl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限码
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限模块id
     */
    private Integer aclModuleId;

    /**
     * 请求的url，可以填正则表达示
     */
    private String url;

    /**
     * 类型，1：菜单，2：按钮，3：其它
     */
    private Integer type;

    /**
     * 状态，1：正常，0：冻结
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最后一次的操作者
     */
    private String operator;

    /**
     * 最后一次的操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 最后一次的操作ip
     */
    private String operateIp;


}

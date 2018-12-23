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
 * 权限表、权限模块
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_acl_module")
public class AclModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限模块id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限模块名称
     */
    private String name;

    /**
     * 上级权限模块id
     */
    private Integer parentId;

    /**
     * 权限模块层级
     */
    private String level;

    /**
     * 排序字段
     */
    private Integer seq;

    /**
     * 状态，1：正常，0：冻结
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 操作日期
     */
    private LocalDateTime operateTime;

    /**
     * 操作ip
     */
    private String operateIp;


}

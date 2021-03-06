package com.idreamsky.permission.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.AutomapConstructor;

/**
 * <p>
 * 角色权限相关表
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@TableName("sys_role_acl")
public class RoleAcl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer aclId;

    /**
     * 最后一次操作者
     */
    private String operator;

    /**
     * 最后一次操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 最后一次操作ip
     */
    private String operateIp;


    public RoleAcl(Integer roleId, Integer aclId) {
        this.roleId = roleId;
        this.aclId = aclId;
    }
}

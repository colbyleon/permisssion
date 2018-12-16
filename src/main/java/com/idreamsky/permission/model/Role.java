package com.idreamsky.permission.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 类型，1：管理员，2：其它
     */
    private Integer type;

    /**
     * 状态，1：正常，2：冻结
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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


}

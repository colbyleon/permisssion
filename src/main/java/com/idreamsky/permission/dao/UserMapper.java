package com.idreamsky.permission.dao;

import com.idreamsky.permission.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据keyword 查询用户名或邮箱
     * @param keyword 用户名或邮箱
     * @return 用户
     */
    User findByKeyword(@Param("keyword") String keyword);

    /**
     * 查找重复的邮箱
     * @param mail 邮箱号
     * @param id 用户id
     * @return 邮箱数量
     */
    int countByMail(@Param("mail") String mail,@Param("id")Integer id);

    /**
     * 查找重复的手机号
     * @param telephone 手机号
     * @param id 用户id
     * @return 手机数量
     */
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);
}

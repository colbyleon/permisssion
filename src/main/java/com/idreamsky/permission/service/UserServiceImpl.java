package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.dao.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User> {

}
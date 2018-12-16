package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idreamsky.permission.model.Log;
import com.idreamsky.permission.dao.LogMapper;
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
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements IService<Log> {

}

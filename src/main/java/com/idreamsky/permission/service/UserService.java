package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Preconditions;
import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.beans.PageResult;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.UserMapper;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.UserParam;
import com.idreamsky.permission.util.BeanValidator;
import com.idreamsky.permission.util.EncryptUtil;
import com.idreamsky.permission.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private LogService logService;

    public void save(UserParam param) {
        BeanValidator.check(param);
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkPhoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        String password = "12345678";
        password = EncryptUtil.md5HexString(password);
        User user = User.builder().username(param.getUsername()).telephone(param.getTelephone()).status(param.getStatus())
                .mail(param.getMail()).deptId(param.getDeptId()).remark(param.getRemark()).password(password).build();

        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(LocalDateTime.now());

        // TODO: sendEmail

        userMapper.insert(user);
        logService.saveUserLog(null,user);
    }

    public void update(UserParam param) {
        BeanValidator.check(param);
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("电话已被占用");
        }
        if (checkPhoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        User before = userMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");

        User after = User.builder().id(param.getId()).username(param.getUsername()).telephone(param.getTelephone()).status(param.getStatus())
                .mail(param.getMail()).deptId(param.getDeptId()).password(before.getPassword()).remark(param.getRemark()).build();

        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(LocalDateTime.now());

        userMapper.updateById(after);
        logService.saveUserLog(before,after);
    }

    public User findByKeyword(String keyword) {
        return userMapper.findByKeyword(keyword);
    }

    public PageResult<User> getPageByDeptId(int deptId, PageQuery page) {
        BeanValidator.check(page);
        Integer count = userMapper.selectCount(Wrappers.<User>query().eq("dept_id", deptId));
        if (count > 0) {
            Integer offset = page.getOffset();
            List<User> users = userMapper.selectList(Wrappers.<User>query()
                    .eq("dept_id", deptId).last("limit " + offset + "," + page.getPageSize()));
            return PageResult.<User>builder().total(count).data(users).build();
        }
        return PageResult.<User>builder().build();
    }

    public List<User> getAll(){
        return userMapper.selectList(Wrappers.emptyWrapper());
    }

    private boolean checkEmailExist(String mail, Integer userId) {
        return userMapper.countByMail(mail, userId) > 0;
    }

    private boolean checkPhoneExist(String phone, Integer userId) {
        return userMapper.countByTelephone(phone, userId) > 0;
    }
}

package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Preconditions;
import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.beans.PageResult;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.AclMapper;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.Acl;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.AclParam;
import com.idreamsky.permission.util.BeanValidator;
import com.idreamsky.permission.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class AclService {
    @Resource
    private AclMapper aclMapper;
    @Resource
    private LogService logService;

    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限名");
        }
        Acl acl = Acl.builder().name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).seq(param.getSeq()).build();

        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(LocalDateTime.now());

        aclMapper.insert(acl);
        logService.saveAclLog(null, acl);
    }

    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限名");
        }

        Acl before = aclMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");

        Acl after = Acl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).seq(param.getSeq()).build();

        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(LocalDateTime.now());

        aclMapper.updateById(after);
        logService.saveAclLog(before, after);
    }

    public PageResult<Acl> getPageByAclModuleId(int aclModuleId, PageQuery page) {
        BeanValidator.check(page);
        int count = aclMapper.selectCount(Wrappers.<Acl>query().eq("acl_module_id", aclModuleId));
        if (count > 0) {
            Integer offset = page.getOffset();
            List<Acl> aclList = aclMapper.selectList(Wrappers.<Acl>query()
                    .eq("acl_module_id", aclModuleId)
                    .orderByAsc("seq","name")
                    .last("limit " + offset + "," + page.getPageSize()));
            return PageResult.<Acl>builder().total(count).data(aclList).build();
        }
        return PageResult.<Acl>builder().build();
    }

    private boolean checkExist(int aclModuleId, String name, Integer id) {
        QueryWrapper<Acl> query = Wrappers.<Acl>query().eq("acl_module_id", aclModuleId)
                .eq("name", name);
        if (id != null) {
            query.ne("id", id);
        }
        return aclMapper.selectCount(query) > 0;
    }

    private String generateCode() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "_" + (int) (Math.random() * 100);
    }
}

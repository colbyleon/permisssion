package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Preconditions;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.AclMapper;
import com.idreamsky.permission.dao.AclModuleMapper;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.Acl;
import com.idreamsky.permission.model.AclModule;
import com.idreamsky.permission.model.Dept;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.AclModuleParam;
import com.idreamsky.permission.util.BeanValidator;
import com.idreamsky.permission.util.IpUtil;
import com.idreamsky.permission.util.LevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 权限表、权限模块 服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class AclModuleService {
    @Resource
    private AclModuleMapper aclModuleMapper;
    @Resource
    private AclMapper aclMapper;

    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        AclModule aclModule = AclModule.builder().name(param.getName()).parentId(param.getParentId())
                .remark(param.getRemark()).seq(param.getSeq()).status(param.getStatus()).build();

        aclModule.setLevel(getLevel(aclModule.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(LocalDateTime.now());

        aclModuleMapper.insert(aclModule);
    }

    public void update(AclModuleParam param) {
        BeanValidator.check(param);

        AclModule before = aclModuleMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");

        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        AclModule after = AclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .remark(param.getRemark()).seq(param.getSeq()).status(param.getStatus()).build();

        after.setLevel(getLevel(param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(LocalDateTime.now());

        updateWithChild(before, after);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void updateWithChild(AclModule before, AclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            // 查出所有子模块
            List<AclModule> subAclModuleList = aclModuleMapper.selectList(Wrappers.<AclModule>query().like("level", oldLevelPrefix + ".%"));
            if (CollectionUtils.isNotEmpty(subAclModuleList)) {
                for (AclModule aclModule : subAclModuleList) {
                    // 替换权限模块level前缀
                    aclModule.setLevel(aclModule.getLevel().replace(oldLevelPrefix, newLevelPrefix));
                }
                aclModuleMapper.batchUpdateLevel(subAclModuleList);
            }
        }
        aclModuleMapper.updateById(after);
    }

    public void delete(int aclModuleId) {
        AclModule aclModule = aclModuleMapper.selectById(aclModuleId);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在");

        Integer subModuleCount = aclModuleMapper.selectCount(Wrappers.<AclModule>query().eq("parent_id", aclModuleId));
        if (subModuleCount > 0) {
            throw new ParamException("当前权限模块下还有子权限模块，无法删除");
        }
        Integer aclCount = aclMapper.selectCount(Wrappers.<Acl>query().eq("acl_module_id", aclModuleId));
        if (aclCount > 0) {
            throw new ParamException("当前权限模块下还有权限点，无法删除");
        }
        aclModuleMapper.deleteById(aclModuleId);
    }

    private boolean checkExist(Integer parentId, String name, Integer id) {
        HashMap<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("parent_id", parentId);
        paramMap.put("name", name);
        QueryWrapper<AclModule> queryCondition = Wrappers.<AclModule>query()
                .allEq(paramMap, false);
        if (id != null) {
            queryCondition.ne("id", id);
        }
        Integer count = aclModuleMapper.selectCount(queryCondition);
        return count > 0;
    }

    private String getLevel(Integer parentId) {
        AclModule aclModule = aclModuleMapper.selectById(parentId);
        String parentLevel = "";
        if (aclModule != null) {
            parentLevel = aclModule.getLevel();
        }
        return LevelUtil.calculateLevel(parentLevel, parentId);
    }
}

package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Preconditions;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.DeptMapper;
import com.idreamsky.permission.dao.UserMapper;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.Dept;
import com.idreamsky.permission.model.User;
import com.idreamsky.permission.param.DeptParam;
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
 * 服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class DeptService {

    @Resource
    private DeptMapper deptMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private LogService logService;

    public void save(DeptParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        Dept dept = Dept.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .remark(param.getRemark()).build();

        dept.setLevel(getLevel(param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(LocalDateTime.now());

        deptMapper.insert(dept);
        logService.saveDeptLog(null, dept);
    }

    public void update(DeptParam param) {
        BeanValidator.check(param);

        Dept before = deptMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");

        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        Dept after = Dept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .remark(param.getRemark()).build();

        after.setLevel(getLevel(param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(LocalDateTime.now());
        updateWithChild(before, after);
        logService.saveDeptLog(before, after);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void updateWithChild(Dept before, Dept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            // 查出所有子部门
            List<Dept> subDeptList = deptMapper.selectList(Wrappers.<Dept>query().likeRight("level", oldLevelPrefix));
            if (CollectionUtils.isNotEmpty(subDeptList)) {
                for (Dept dept : subDeptList) {
                    // 替换部门level前缀
                    dept.setLevel(dept.getLevel().replace(oldLevelPrefix, newLevelPrefix));
                }
                deptMapper.batchUpdateLevel(subDeptList);
            }
        }
        deptMapper.updateById(after);
    }

    public void delete(int deptId) {
        Dept dept = deptMapper.selectById(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在");

        Integer subDeptCount = deptMapper.selectCount(Wrappers.<Dept>query().eq("parent_id", deptId));
        if (subDeptCount > 0) {
            throw new ParamException("当前部门下还有子部门，无法删除");
        }
        Integer userCount = userMapper.selectCount(Wrappers.<User>query().eq("dept_id", deptId));
        if (userCount > 0) {
            throw new ParamException("当前部门下还有用户，无法删除");
        }

        deptMapper.deleteById(deptId);
        logService.saveDeptLog(dept, null);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("parent_id", parentId);
        paramMap.put("name", deptName);
        QueryWrapper<Dept> queryCondition = Wrappers.<Dept>query()
                .allEq(paramMap, false);
        if (deptId != null) {
            queryCondition.ne("id", deptId);
        }
        Integer count = deptMapper.selectCount(queryCondition);
        return count > 0;
    }

    private String getLevel(Integer parentId) {
        Dept dept = deptMapper.selectById(parentId);
        String parentLevel = "";
        if (dept != null) {
            parentLevel = dept.getLevel();
        }
        return LevelUtil.calculateLevel(parentLevel, parentId);
    }
}

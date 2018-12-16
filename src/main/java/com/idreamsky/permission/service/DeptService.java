package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.Dept;
import com.idreamsky.permission.dao.DeptMapper;
import com.idreamsky.permission.param.DeptParam;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idreamsky.permission.util.BeanValidator;
import com.idreamsky.permission.util.LevelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Service
public class DeptService {

    @Resource
    private DeptMapper deptMapper;

    public void save(DeptParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        Dept dept = Dept.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .remark(param.getRemark()).build();

        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));

        //TODO:
        dept.setOperator("system");
        dept.setOperateIp("127.0.0.1");

        dept.setOperateTime(LocalDateTime.now());

        deptMapper.insert(dept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        // TODO:
        return true;
    }

    private String getLevel(Integer parentId) {
        Dept dept = deptMapper.selectById(parentId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }
}

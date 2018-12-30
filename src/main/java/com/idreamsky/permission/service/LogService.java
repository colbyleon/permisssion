package com.idreamsky.permission.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.idreamsky.permission.beans.LogType;
import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.beans.PageResult;
import com.idreamsky.permission.common.RequestHolder;
import com.idreamsky.permission.dao.*;
import com.idreamsky.permission.dto.SearchLogDto;
import com.idreamsky.permission.exception.ParamException;
import com.idreamsky.permission.model.*;
import com.idreamsky.permission.param.SearchLogParam;
import com.idreamsky.permission.util.BeanValidator;
import com.idreamsky.permission.util.IpUtil;
import com.idreamsky.permission.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
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
public class LogService {
    @Resource
    private LogMapper logMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AclModuleMapper aclModuleMapper;
    @Resource
    private AclMapper aclMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleAclService roleAclService;
    @Resource
    private RoleUserService roleUserService;


    public PageResult<LogWithBlobs> searchPageList(SearchLogParam param, PageQuery page) {
        BeanValidator.check(param);
        SearchLogDto dto = new SearchLogDto();
        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeg())) {
            dto.setBeforeSeg("%" + param.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getAfterSeg())) {
            dto.setAfterSeg("%" + param.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            dto.setOperator("%" + param.getOperator() + "%");
        }
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFromTime())) {
                dto.setFromTime(LocalDateTime.from(dtf.parse(param.getFromTime())));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                dto.setToTime(LocalDateTime.from(dtf.parse(param.getToTime())));
            }
        } catch (Exception e) {
            throw new ParamException("传入的日期格式错误,正确模式为yyyy-MM-dd HH:mm:ss");
        }

        int count = logMapper.countBySearchDto(dto);
        if (count > 0) {
            List<LogWithBlobs> logs = logMapper.getPageListBySearchDto(dto, page);
            return PageResult.<LogWithBlobs>builder().total(count).data(logs).build();
        }
        return PageResult.<LogWithBlobs>builder().build();
    }

    public void recover(int logId){
        LogWithBlobs log = logMapper.selectById(logId);
        Preconditions.checkNotNull(log, "待还原的记录不存在");
        switch (log.getType()){
            case LogType.TYPE_DEPT:
                Dept beforeDept = deptMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门已经不存在了");
                if (StringUtils.isBlank(log.getNewValue())  || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                Dept afterDept = JsonMapper.parseObject(log.getOldValue(), new TypeReference<Dept>() {
                });
                afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterDept.setOperateTime(LocalDateTime.now());
                deptMapper.updateById(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                User beforeUser = userMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户已经不存在了");
                if (StringUtils.isBlank(log.getNewValue())  || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                User afterUser = JsonMapper.parseObject(log.getOldValue(), new TypeReference<User>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(LocalDateTime.now());
                userMapper.updateById(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ACL_MODULE:
                AclModule beforeAclModule = aclModuleMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(beforeAclModule, "待还原的权限模块已经不存在了");
                if (StringUtils.isBlank(log.getNewValue())  || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                AclModule afterAclModule = JsonMapper.parseObject(log.getOldValue(), new TypeReference<AclModule>() {
                });
                afterAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAclModule.setOperateTime(LocalDateTime.now());
                aclModuleMapper.updateById(afterAclModule);
                saveAclModuleLog(beforeAclModule, afterAclModule);
                break;
            case LogType.TYPE_ACL:
                Acl beforeAcl = aclMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限点已经不存在了");
                if (StringUtils.isBlank(log.getNewValue())  || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                Acl afterAcl = JsonMapper.parseObject(log.getOldValue(), new TypeReference<Acl>() {
                });
                afterAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAcl.setOperateTime(LocalDateTime.now());
                aclMapper.updateById(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;
            case LogType.TYPE_ROLE:
                Role beforeRole = roleMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色已经不存在了");
                if (StringUtils.isBlank(log.getNewValue())  || StringUtils.isBlank(log.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                Role afterRole = JsonMapper.parseObject(log.getOldValue(), new TypeReference<Role>() {
                });
                afterRole.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterRole.setOperateTime(LocalDateTime.now());
                roleMapper.updateById(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ROLE_ACL:
                Role aclRole = roleMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(aclRole, "角色已经不存在了");
                roleAclService.changeRoleAcls(log.getTargetId(), JsonMapper.parseObject(log.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            case LogType.TYPE_ROLE_USER:
                Role userRole = roleMapper.selectById(log.getTargetId());
                Preconditions.checkNotNull(userRole, "角色已经不存在了");
                roleUserService.changeRoleUsers(log.getTargetId(), JsonMapper.parseObject(log.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
            default:;
        }
    }

    public void saveDeptLog(Dept before, Dept after) {
        LogWithBlobs log = new LogWithBlobs();
        log.setType(LogType.TYPE_DEPT);
        log.setTargetId(after == null ? before.getId() : after.getId());
        log.setOldValue(before == null ? "" : JsonMapper.toJSONString(before));
        log.setNewValue(after == null ? "" : JsonMapper.toJSONString(after));
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(LocalDateTime.now());
        log.setStatus(1);
        logMapper.insert(log);
    }

    public void saveUserLog(User before, User after) {
        LogWithBlobs log = new LogWithBlobs();
        log.setType(LogType.TYPE_USER);
        log.setTargetId(after == null ? before.getId() : after.getId());
        log.setOldValue(before == null ? "" : JsonMapper.toJSONString(before));
        log.setNewValue(after == null ? "" : JsonMapper.toJSONString(after));
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(LocalDateTime.now());
        log.setStatus(1);
        logMapper.insert(log);
    }

    public void saveAclModuleLog(AclModule before, AclModule after) {
        LogWithBlobs log = new LogWithBlobs();
        log.setType(LogType.TYPE_ACL_MODULE);
        log.setTargetId(after == null ? before.getId() : after.getId());
        log.setOldValue(before == null ? "" : JsonMapper.toJSONString(before));
        log.setNewValue(after == null ? "" : JsonMapper.toJSONString(after));
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(LocalDateTime.now());
        log.setStatus(1);
        logMapper.insert(log);
    }

    public void saveAclLog(Acl before, Acl after) {
        LogWithBlobs log = new LogWithBlobs();
        log.setType(LogType.TYPE_ACL);
        log.setTargetId(after == null ? before.getId() : after.getId());
        log.setOldValue(before == null ? "" : JsonMapper.toJSONString(before));
        log.setNewValue(after == null ? "" : JsonMapper.toJSONString(after));
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(LocalDateTime.now());
        log.setStatus(1);
        logMapper.insert(log);
    }

    public void saveRoleLog(Role before, Role after) {
        LogWithBlobs log = new LogWithBlobs();
        log.setType(LogType.TYPE_ROLE);
        log.setTargetId(after == null ? before.getId() : after.getId());
        log.setOldValue(before == null ? "" : JsonMapper.toJSONString(before));
        log.setNewValue(after == null ? "" : JsonMapper.toJSONString(after));
        log.setOperator(RequestHolder.getCurrentUser().getUsername());
        log.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        log.setOperateTime(LocalDateTime.now());
        log.setStatus(1);
        logMapper.insert(log);
    }

}

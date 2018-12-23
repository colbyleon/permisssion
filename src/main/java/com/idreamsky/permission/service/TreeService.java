package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.idreamsky.permission.dao.AclMapper;
import com.idreamsky.permission.dao.AclModuleMapper;
import com.idreamsky.permission.dao.DeptMapper;
import com.idreamsky.permission.dto.AclDto;
import com.idreamsky.permission.dto.AclModuleLevelDto;
import com.idreamsky.permission.dto.DeptLevelDto;
import com.idreamsky.permission.model.Acl;
import com.idreamsky.permission.model.AclModule;
import com.idreamsky.permission.model.Dept;
import com.idreamsky.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: colby
 * @Date: 2018/12/16 16:15
 */
@Service
public class TreeService {

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private AclModuleMapper aclModuleMapper;

    @Resource
    private CoreService coreService;

    @Resource
    private AclMapper aclMapper;

    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1、当前用户已分配的权限点
        List<Acl> userAclList = coreService.getCurrentUserAclList();
        // 2、当前角色分配的权限点
        List<Acl> roleAclList = coreService.getRoleAclList(roleId);
        // 3、取出所有权限
        List<Acl> acls = aclMapper.selectList(Wrappers.emptyWrapper());

        List<AclDto> aclDtoList = Lists.newArrayList();
        for (Acl acl : acls) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclList.contains(acl)) {
                dto.setHasAcl(true);
            }
            if (roleAclList.contains(acl)) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();

        for (AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    private void bindAclWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(aclModuleLevelDto.getId());
            // 排序
            aclDtoList.sort(Comparator.comparingInt(Acl::getSeq));
            // 绑定当前权限模块下的模块
            aclModuleLevelDto.setAclList(aclDtoList);
            // 递归绑定子权限模块
            bindAclWithOrder(aclModuleLevelDto.getAclModuleList(), moduleIdAclMap);
        }
    }


    /**
     * 权限模块树
     */
    public List<AclModuleLevelDto> aclModuleTree() {
        List<AclModule> aclModules = aclModuleMapper.selectList(Wrappers.emptyWrapper());
        List<AclModuleLevelDto> dtoList = aclModules.stream().map(AclModuleLevelDto::adapt).collect(Collectors.toList());
        return aclModuleListToTree(dtoList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> aclModuleLevelDtoList) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return Lists.newArrayList();
        }
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        for (AclModuleLevelDto dto : aclModuleLevelDtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);
        }

        List<AclModuleLevelDto> rootList = (List<AclModuleLevelDto>) levelAclModuleMap.get(LevelUtil.ROOT);

        rootList.sort(Comparator.comparingInt(AclModule::getSeq));
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;
    }

    private void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelDtoList, String level, Multimap<String, AclModuleLevelDto> levelAclModuleMap) {
        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            List<AclModuleLevelDto> subAclModules = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(subAclModules)) {
                subAclModules.sort(Comparator.comparingInt(AclModuleLevelDto::getSeq));
                aclModuleLevelDto.setAclModuleList(subAclModules);
                transformAclModuleTree(subAclModules, nextLevel, levelAclModuleMap);
            }
        }
    }

    /**
     * 部门树
     */
    public List<DeptLevelDto> deptTree() {
        List<Dept> depts = deptMapper.selectList(Wrappers.emptyWrapper());
        List<DeptLevelDto> dtoList = depts.stream().map(DeptLevelDto::adapt).collect(Collectors.toList());
        return deptListToTree(dtoList);
    }

    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1,dept2,...] 区别于普通map的是 相同key存入value的集合中，而不是覆盖
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        // 赋值
        deptLevelList.forEach(dto -> levelDeptMap.put(dto.getLevel(), dto));
        // root级
        List<DeptLevelDto> rootList = (List<DeptLevelDto>) levelDeptMap.get(LevelUtil.ROOT);
        // 按照seq从小到大进行排序
        rootList.sort(Comparator.comparingInt(Dept::getSeq));
        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    private void transformDeptTree(List<DeptLevelDto> deptLevelList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        for (DeptLevelDto deptLevelDto : deptLevelList) {
            // 计算出下一个层级的level
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 取出当前dept的下一层，没有则过
            List<DeptLevelDto> subDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(subDeptList)) {
                // 排序
                subDeptList.sort(Comparator.comparingInt(Dept::getSeq));
                // 设置下一层部门
                deptLevelDto.setDeptList(subDeptList);
                // 进入到下一层去处理
                transformDeptTree(subDeptList, nextLevel, levelDeptMap);
            }
        }
    }
}

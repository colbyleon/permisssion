package com.idreamsky.permission.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.idreamsky.permission.dao.DeptMapper;
import com.idreamsky.permission.dto.DeptLevelDto;
import com.idreamsky.permission.model.Dept;
import com.idreamsky.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<DeptLevelDto> deptTree() {
        List<Dept> depts = deptMapper.selectList(Wrappers.query());

        List<DeptLevelDto> dtoList = depts.stream().map(DeptLevelDto::adapt).collect(Collectors.toList());

        return deptListToTree(dtoList);
    }

    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        // level -> [dept1,dept2,...]
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        ArrayList<DeptLevelDto> rootList = Lists.newArrayList();

        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 按照seq从小到大进行排序
        rootList.sort(Comparator.comparingInt(Dept::getSeq));
        // 递归生成树
        transformDeptTree(rootList,LevelUtil.ROOT,levelDeptMap);
        return rootList;
    }

    public void transformDeptTree(List<DeptLevelDto> deptLevelList, String level, Multimap<String, DeptLevelDto> levelDeptMap) {
        for (DeptLevelDto deptLevelDto : deptLevelList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一层
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

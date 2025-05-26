package com.shiyuan.base.modules.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.modules.permission.entity.VRole;
import com.shiyuan.base.modules.permission.mapper.VRoleMapper;
import com.shiyuan.base.modules.permission.service.VRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangshiyuan
 * @description 针对表【v_role】的数据库操作Service实现
 * @createDate 2025-04-29 17:17:05
 */
@Service
public class VRoleServiceImpl extends ServiceImpl<VRoleMapper, VRole> implements VRoleService {
    @Autowired
    private VRoleMapper roleMapper;

    @Override
    public String getRoleCodeByUserId(Long userId) {
        return roleMapper.selectRoleCodeByUserId(userId);
    }
}

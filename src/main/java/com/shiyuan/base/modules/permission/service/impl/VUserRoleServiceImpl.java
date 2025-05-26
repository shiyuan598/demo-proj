package com.shiyuan.base.modules.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.modules.permission.entity.VUserRole;
import com.shiyuan.base.modules.permission.mapper.VUserRoleMapper;
import com.shiyuan.base.modules.permission.service.VUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author wangshiyuan
 * @description 针对表【v_user_role】的数据库操作Service实现
 * @createDate 2025-04-29 17:17:05
 */
@Service
public class VUserRoleServiceImpl extends ServiceImpl<VUserRoleMapper, VUserRole> implements VUserRoleService {

    @Override
    public void updateUserRole(Long userId, Long roleId) {
        // 先删除旧的角色绑定
        this.remove(new LambdaQueryWrapper<VUserRole>().eq(VUserRole::getUserId, userId));

        // 再插入新的角色绑定
        VUserRole userRole = new VUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        this.save(userRole);
    }
}

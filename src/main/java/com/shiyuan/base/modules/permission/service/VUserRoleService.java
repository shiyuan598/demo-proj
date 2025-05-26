package com.shiyuan.base.modules.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyuan.base.modules.permission.entity.VUserRole;

/**
 * @author wangshiyuan
 * @description 针对表【v_user_role】的数据库操作Service
 * @createDate 2025-04-29 17:17:05
 */
public interface VUserRoleService extends IService<VUserRole> {
    public void updateUserRole(Long userId, Long roleId);
}

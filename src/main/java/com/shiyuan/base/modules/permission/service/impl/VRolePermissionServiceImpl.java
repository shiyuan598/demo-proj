package com.shiyuan.base.modules.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.modules.permission.entity.VRolePermission;
import com.shiyuan.base.modules.permission.mapper.VRolePermissionMapper;
import com.shiyuan.base.modules.permission.service.VRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * @author wangshiyuan
 * @description 针对表【v_role_permission】的数据库操作Service实现
 * @createDate 2025-04-29 17:17:05
 */
@Service
public class VRolePermissionServiceImpl extends ServiceImpl<VRolePermissionMapper, VRolePermission>
    implements VRolePermissionService {}

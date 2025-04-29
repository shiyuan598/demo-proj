package com.shiyuan.base.modules.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_role_permission
 */
@TableName(value ="v_role_permission")
@Data
public class VRolePermission {
    private Long roleId;

    private Long permissionId;
}
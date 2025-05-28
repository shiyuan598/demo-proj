package com.shiyuan.base.modules.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_user_role
 */
@TableName(value = "v_user_role")
@Data
public class VUserRole {
    @TableId(value = "id", type = IdType.AUTO)
    private Long userId;

    private Long roleId;
}

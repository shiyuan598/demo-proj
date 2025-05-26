package com.shiyuan.base.modules.permission.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_role
 */
@TableName(value = "v_role")
@Data
public class VRole {
    private Long id;

    private String name;

    private String code;

    private String remark;
}

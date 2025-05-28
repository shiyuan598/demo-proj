package com.shiyuan.base.modules.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_permission
 */
@TableName(value = "v_permission")
@Data
public class VPermission {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String url;

    private String method;

    private String code;
}

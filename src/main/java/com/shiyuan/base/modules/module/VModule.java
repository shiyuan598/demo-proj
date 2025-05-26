package com.shiyuan.base.modules.module;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_module
 */
@TableName(value = "v_module")
@Data
public class VModule {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer pid;
}

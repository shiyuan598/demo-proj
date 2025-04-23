package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_module
 */
@TableName(value ="v_module")
@Data
public class Module {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer pid;
}
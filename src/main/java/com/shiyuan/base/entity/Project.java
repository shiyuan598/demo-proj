package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_project
 */
@TableName(value ="v_project")
@Data
public class Project {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
}
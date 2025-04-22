package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_project
 */
@TableName(value ="v_project")
@Data
public class Project {
    private Integer id;

    private String name;
}
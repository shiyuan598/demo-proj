package com.shiyuan.base.modules.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_project
 */
@TableName(value ="v_project")
@Data
public class VProject {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
}
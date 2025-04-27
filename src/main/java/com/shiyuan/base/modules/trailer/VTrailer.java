package com.shiyuan.base.modules.trailer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_trailer
 */
@TableName(value ="v_trailer")
@Data
public class VTrailer {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
}
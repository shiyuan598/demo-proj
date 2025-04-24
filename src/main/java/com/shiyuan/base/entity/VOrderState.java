package com.shiyuan.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_order_state
 */
@TableName(value ="v_order_state")
@Data
public class VOrderState {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer state;

    private String name;
}
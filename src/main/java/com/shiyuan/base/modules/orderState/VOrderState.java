package com.shiyuan.base.modules.orderState;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName v_order_state
 */
@TableName(value = "v_order_state")
@Data
public class VOrderState {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer state;

    private String name;
}

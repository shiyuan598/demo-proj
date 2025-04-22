package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.OrderState;
import com.shiyuan.base.service.OrderStateService;
import com.shiyuan.base.mapper.OrderStateMapper;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_order_state(订单状态)】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class OrderStateServiceImpl extends ServiceImpl<OrderStateMapper, OrderState>
    implements OrderStateService{

}





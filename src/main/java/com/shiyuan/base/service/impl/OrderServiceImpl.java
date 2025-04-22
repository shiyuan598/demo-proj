package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.Order;
import com.shiyuan.base.service.OrderService;
import com.shiyuan.base.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_order(订单表)】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}





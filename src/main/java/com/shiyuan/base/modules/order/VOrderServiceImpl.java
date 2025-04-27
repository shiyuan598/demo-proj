package com.shiyuan.base.modules.order;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.modules.order.mapper.VOrderMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_order(订单表)】的数据库操作Service实现
* @createDate 2025-04-24 13:40:41
*/
@Service
@Primary
public class VOrderServiceImpl extends ServiceImpl<VOrderMapper, VOrder>
    implements VOrderService{

}





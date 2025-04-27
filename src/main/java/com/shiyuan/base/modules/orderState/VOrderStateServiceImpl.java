package com.shiyuan.base.modules.orderState;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.service.VOrderStateService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_order_state(订单状态)】的数据库操作Service实现
* @createDate 2025-04-24 13:40:42
*/
@Service
@Primary
public class VOrderStateServiceImpl extends ServiceImpl<VOrderStateMapper, VOrderState>
    implements VOrderStateService{

}





package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyuan.base.entity.Vehicle;
import com.shiyuan.base.service.VehicleService;
import com.shiyuan.base.mapper.VehicleMapper;
import org.springframework.stereotype.Service;

/**
* @author wangshiyuan
* @description 针对表【v_vehicle】的数据库操作Service实现
* @createDate 2025-04-22 16:42:19
*/
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle>
    implements VehicleService{

}





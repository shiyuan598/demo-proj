package com.shiyuan.base.service;

import com.shiyuan.base.entity.VVehicle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyuan.base.entity.vo.vehicle.VVehicleDictVO;

import java.util.List;

/**
* @author wangshiyuan
* @description 针对表【v_vehicle】的数据库操作Service
* @createDate 2025-04-24 13:40:42
*/
public interface VVehicleService extends IService<VVehicle> {
    List<VVehicleDictVO> getDictVO(String blurry);
}

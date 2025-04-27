package com.shiyuan.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.entity.VVehicle;
import com.shiyuan.base.entity.converter.VehicleConverter;
import com.shiyuan.base.entity.vo.vehicle.VVehicleDictVO;
import com.shiyuan.base.mapper.VVehicleMapper;
import com.shiyuan.base.service.VVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author wangshiyuan
* @description 针对表【v_vehicle】的数据库操作Service实现
* @createDate 2025-04-24 13:40:42
*/
@Service
public class VVehicleServiceImpl extends ServiceImpl<VVehicleMapper, VVehicle>
    implements VVehicleService{

    @Autowired
    private VehicleConverter vehicleConverter;

    @Transactional(readOnly = true)
    @Override
    public List<VVehicleDictVO> getDictVO(String blurry) {
        LambdaQueryWrapper<VVehicle> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(blurry)) {
            wrapper.like(VVehicle::getVehicleNo, blurry);
        }
        wrapper.eq(VVehicle::getState, 1);
        wrapper.select(VVehicle::getId, VVehicle::getVehicleNo, VVehicle::getProject, VVehicle::getPlace, VVehicle::getState);
        wrapper.orderByDesc(VVehicle::getCreateTime);

        List<VVehicle> vehicleList = this.list(wrapper);
        return vehicleConverter.toDictVOList(vehicleList);
    }
}





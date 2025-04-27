package com.shiyuan.base.entity.converter;

import com.shiyuan.base.entity.VVehicle;
import com.shiyuan.base.entity.vo.vehicle.VVehicleDictVO;
import com.shiyuan.base.entity.vo.vehicle.VVehicleVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleConverter {
    // Entity 转 DictVO
    VVehicleDictVO toDictVO(VVehicle vehicle);

    // Entity 转 VO
    VVehicleVO toVO(VVehicle vehicle);

    // List<Entity> 转 List<VO>
    List<VVehicleDictVO> toDictVOList(List<VVehicle> vehicleList);
}

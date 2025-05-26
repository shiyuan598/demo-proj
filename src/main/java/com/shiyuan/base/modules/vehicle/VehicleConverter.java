package com.shiyuan.base.modules.vehicle;

import com.shiyuan.base.modules.vehicle.dto.VVehicleAddDTO;
import com.shiyuan.base.modules.vehicle.dto.VVehicleUpdateDTO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleDictVO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleVO;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleConverter {
    // VehicleAddDTO 转 Entity
    @Mapping(target = "id", ignore = true)
    VVehicle toEntity(VVehicleAddDTO vehicle);

    // Entity 转 DictVO
    VVehicleDictVO toDictVO(VVehicle vehicle);

    // Entity 转 VO
    VVehicleVO toVO(VVehicle vehicle);

    // List<Entity> 转 List<VO>
    List<VVehicleDictVO> toDictVOList(List<VVehicle> vehicleList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVehicleFromDto(VVehicleUpdateDTO source, @MappingTarget VVehicle target);
}

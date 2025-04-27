package com.shiyuan.base.modules.vehicle.vo;

import com.shiyuan.base.modules.vehicle.VVehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "车辆")
public class VVehicleVO extends VVehicle {
    private String stateName;
    private String projectName;
}

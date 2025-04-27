package com.shiyuan.base.entity.vo.vehicle;

import com.shiyuan.base.entity.VVehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "车辆")
public class VVehicleVO extends VVehicle {
    private String stateName;
    private String projectName;
}

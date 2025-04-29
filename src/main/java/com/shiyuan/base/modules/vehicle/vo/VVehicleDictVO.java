package com.shiyuan.base.modules.vehicle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "车辆字典")
public class VVehicleDictVO {
    private Long id;

    private String vehicleNo;

    private String project;

    private String place;

    private Integer state;
}

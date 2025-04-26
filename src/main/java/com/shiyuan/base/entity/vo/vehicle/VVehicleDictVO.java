package com.shiyuan.base.entity.vo.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "车辆字典")
public class VVehicleDictVO {
    private Integer id;

    private String vehicleNo;

    private String project;

    private String place;

    private Integer state;
}

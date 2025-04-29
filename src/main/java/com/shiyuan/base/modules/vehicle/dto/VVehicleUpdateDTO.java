package com.shiyuan.base.modules.vehicle.dto;

import com.shiyuan.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VVehicleUpdateDTO extends BaseDTO {
    private Long id;

    private String vehicleNo;

    private String project;

    private String place;

    private Integer state;

    private String reason;

    private Integer maintainer;
}
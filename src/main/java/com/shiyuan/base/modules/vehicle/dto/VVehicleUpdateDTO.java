package com.shiyuan.base.modules.vehicle.dto;

import lombok.Data;

@Data
public class VVehicleUpdateDTO {
    private Integer id;

    private String vehicleNo;

    private String project;

    private String place;

    private Integer state;

    private String reason;

    private Integer maintainer;
}
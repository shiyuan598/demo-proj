package com.shiyuan.base.modules.vehicle.dto;

import com.shiyuan.base.common.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VVehicleAddDTO extends BaseDTO {
    @NotBlank(message = "车辆编号不能为空")
    private String vehicleNo;

    @NotNull(message = "所属项目不能为空")
    private Long project;

    @NotBlank(message = "地点不能为空")
    private String place;

    @NotNull(message = "状态不能为空")
    private Integer state = 1;

    private String reason;

    @NotNull(message = "维护人不能为空")
    private Integer maintainer;
}
package com.shiyuan.base.modules.vehicle.vo;

import com.shiyuan.base.common.utils.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "可用车辆列表响应对象")
public class VVehicleListResponse  extends BaseResponse<List<VVehicleDictVO>> {
}

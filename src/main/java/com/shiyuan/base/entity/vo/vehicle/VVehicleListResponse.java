package com.shiyuan.base.entity.vo.vehicle;

import com.shiyuan.base.entity.vo.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "可用车辆列表响应对象")
public class VVehicleListResponse  extends BaseResponse<List<VVehicleDictVO>> {
}

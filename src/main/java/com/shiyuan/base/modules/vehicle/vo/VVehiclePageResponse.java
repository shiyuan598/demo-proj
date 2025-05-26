package com.shiyuan.base.modules.vehicle.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiyuan.base.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "车辆分页列表响应对象")
public class VVehiclePageResponse extends BaseResponse<IPage<VVehicleVO>> {}

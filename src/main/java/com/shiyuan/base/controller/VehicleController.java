package com.shiyuan.base.controller;

import com.shiyuan.base.entity.vo.vehicle.VVehicleListResponse;
import com.shiyuan.base.service.VVehicleService;
import com.shiyuan.base.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "车辆相关")
@RestController
@RequestMapping("/vehicle")
@Slf4j
public class VehicleController {
    @Autowired
    private VVehicleService vehicleService;

    @Operation(summary = "可用车辆列表")
    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VVehicleListResponse.class)))
    public ResponseEntity<ResponseUtils> list(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry) {
        try {
            return ResponseUtils.success(vehicleService.getDictVO(blurry));
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }
}

package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiyuan.base.entity.vo.vehicle.VVehicleDictVO;
import com.shiyuan.base.entity.vo.vehicle.VVehicleListResponse;
import com.shiyuan.base.entity.vo.vehicle.VVehicleVO;
import com.shiyuan.base.service.VVehicleService;
import com.shiyuan.base.util.ResponseResult;
import com.shiyuan.base.util.ResultCode;
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

import java.util.List;

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
    public ResponseEntity<ResponseResult<List<VVehicleDictVO>>> list(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry) {
        try {
            return ResponseEntity.ok(ResponseResult.success(vehicleService.getDictVO(blurry)));
        } catch (Exception e) {
            log.error("查询可用车辆列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "车辆分页列表")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VVehicleListResponse.class)))
    public ResponseEntity<ResponseResult<List<VVehicleVO>>> getVehiclePage(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry,
                                                                           @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") long currentPage,
                                                                           @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") long pageSize,
                                                                           @Parameter(description = "排序字段") @RequestParam(required = false) String sort,
                                                                           @Parameter(description = "排序方向 (asc/desc)") @RequestParam(required = false) String order) {
        try {
            IPage<VVehicleVO> pageVO = vehicleService.getVehiclePage(blurry, currentPage, pageSize, sort, order);
            return ResponseEntity.ok(ResponseResult.success(pageVO));
        } catch (Exception e) {
            log.error("查询用户分页列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }
}

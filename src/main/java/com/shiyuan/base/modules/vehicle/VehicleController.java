package com.shiyuan.base.modules.vehicle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.modules.vehicle.dto.VVehicleAddDTO;
import com.shiyuan.base.modules.vehicle.dto.VVehicleUpdateDTO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleDictVO;
import com.shiyuan.base.modules.vehicle.vo.VVehicleListResponse;
import com.shiyuan.base.modules.vehicle.vo.VVehicleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "车辆相关")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VVehicleService vehicleService;

    @Operation(summary = "可用车辆列表")
    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "查询成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseResult.class)))
    public ResponseEntity<ResponseResult<List<VVehicleDictVO>>>
        list(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry) {
        List<VVehicleDictVO> vehicles = vehicleService.getDictVO(blurry);
        return ResponseEntity.ok(ResponseResult.success(vehicles));
    }

    @Operation(summary = "车辆分页列表")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = VVehicleListResponse.class)))
    public ResponseEntity<ResponseResult<List<VVehicleVO>>> getVehiclePage(
        @Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry,
        @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long currentPage,
        @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") Long pageSize,
        @Parameter(description = "排序字段") @RequestParam(required = false) String sort,
        @Parameter(description = "排序方向 (asc/desc)") @RequestParam(required = false) String order) {
        IPage<VVehicleVO> pageVO = vehicleService.getVehiclePage(blurry, currentPage, pageSize, sort, order);
        return ResponseEntity.ok(ResponseResult.success(pageVO));
    }

    @Operation(summary = "车辆总数")
    @GetMapping("/count")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ResponseResult.class, type = "long")))
    public ResponseEntity<ResponseResult<Long>>
        count(@Parameter(description = "状态") @RequestParam(required = false) Integer state) {
        Long count = vehicleService.getCount(state);
        return ResponseEntity.ok(ResponseResult.success(count));
    }

    @Operation(summary = "添加车辆")
    @PostMapping
    @Validated
    @ApiResponse(responseCode = "200", description = "添加成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    public ResponseEntity<ResponseResult<Long>>
        addVehicle(@Parameter(description = "车辆实体") @Valid @RequestBody VVehicleAddDTO vehicle) {
        Long vehicleId = vehicleService.addVehicle(vehicle);
        return ResponseEntity.ok(ResponseResult.success(vehicleId));
    }

    @Operation(summary = "编辑车辆")
    @PutMapping("/{id}")
    @Validated
    @ApiResponse(responseCode = "200", description = "更新成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    public ResponseEntity<ResponseResult<VVehicle>> updateVehicle(
        @Parameter(description = "车辆Id") @PathVariable Long id,
        @Valid @Parameter(description = "车辆实体") @RequestBody VVehicleUpdateDTO vehicle) {
        VVehicle updatedVehicle = vehicleService.updateVehicle(id, vehicle);
        return ResponseEntity.ok(ResponseResult.success(updatedVehicle));
    }

    @Operation(summary = "删除车辆")
    @DeleteMapping("/{id}")
    @Validated
    @ApiResponse(responseCode = "200", description = "删除成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<ResponseResult<Boolean>>
        deleteVehicle(@Parameter(description = "车辆Id") @PathVariable Long id) {
        Boolean success = vehicleService.removeById(id);
        return ResponseEntity.ok(ResponseResult.success(success));
    }
}

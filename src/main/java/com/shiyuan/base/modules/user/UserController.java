package com.shiyuan.base.modules.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shiyuan.base.common.response.BaseResponse;
import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.common.response.ResultCode;
import com.shiyuan.base.modules.user.dto.VUserAddDTO;
import com.shiyuan.base.modules.user.dto.VUserUpdateDTO;
import com.shiyuan.base.modules.user.vo.VUserVO;
import com.shiyuan.base.modules.user.vo.VUserVOListResponse;
import com.shiyuan.base.modules.user.vo.VUserVOPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户相关")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private VUserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VUserVOListResponse.class)))
    public ResponseEntity<ResponseResult<List<VUserVO>>> list() {
        try {
            List<VUser> userList = userService.list();
            return ResponseEntity.ok(ResponseResult.success(userConverter.toVOList(userList)));
        } catch (Exception e) {
            log.error("查询用户列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "司机列表")
    @GetMapping("/driver")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VUserVOListResponse.class)))
    public ResponseEntity<ResponseResult<List<VUserVO>>> getDrivers(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry) {
        try {
            List<VUserVO> drivers = userService.getDrivers(blurry);
            return ResponseEntity.ok(ResponseResult.success(drivers));
        } catch (Exception e) {
            log.error("查询司机列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "用户分页列表")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VUserVOPageResponse.class)))
    public ResponseEntity<ResponseResult<List<VUserVO>>> getUserPage(
            @Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry,
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long currentPage,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sort,
            @Parameter(description = "排序方向 (asc/desc)") @RequestParam(required = false) String order) {
        try {
            IPage<VUserVO> pageVO = userService.getUserPage(blurry, currentPage, pageSize, sort, order);
            return ResponseEntity.ok(ResponseResult.success(pageVO));
        } catch (Exception e) {
            log.error("查询用户分页列表失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "添加用户")
    @PostMapping
    @Validated
    @ApiResponse(responseCode = "200", description = "添加成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseResult<Long>> addUser(@Parameter(description = "用户信息") @Valid @RequestBody VUserAddDTO vUserDTO) {
        try {
            return ResponseEntity.ok(ResponseResult.success(userService.addUser(vUserDTO)));
        } catch(IllegalArgumentException e) {
            log.error("添加用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.fail(ResultCode.PARAM_ERROR, e.getMessage()));
        } catch (Exception e) {
            log.error("添加用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseResult<VUserVO>> updateUser(@Parameter(description = "用户Id") @PathVariable Long id,
                                                              @Parameter(description = "用户信息") @RequestBody VUserUpdateDTO vUserDTO) {
        try {
            return ResponseEntity.ok(ResponseResult.success(userService.updateUser(id, vUserDTO)));
        } catch(IllegalArgumentException e) {
            log.error("编辑用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.fail(ResultCode.PARAM_ERROR, e.getMessage()));
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "删除成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseResult<Boolean>> deleteUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ResponseResult.success(userService.removeUserById(id)));
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }
}

package com.shiyuan.base.modules.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.common.response.BaseResponse;
import com.shiyuan.base.modules.user.vo.VUserVO;
import com.shiyuan.base.modules.user.vo.VUserVOListResponse;
import com.shiyuan.base.modules.user.vo.VUserVOPageResponse;
import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.common.response.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
            List<VUserVO> voList = userConverter.toVOList(userList);
            return ResponseEntity.ok(ResponseResult.success(voList));
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
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getRole, 2);
            if (StrUtil.isNotBlank(blurry)) {
                wrapper.like(VUser::getName, blurry).or().like(VUser::getUsername, blurry);
            }
            List<VUser> drivers = userService.list(wrapper);
            List<VUserVO> voList = userConverter.toVOList(drivers);
            return ResponseEntity.ok(ResponseResult.success(voList));
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
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") long currentPage,
            @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") long pageSize,
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
    @ApiResponse(responseCode = "200", description = "添加成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseResult<Boolean>> addUser(@Parameter(description = "用户信息") @RequestBody VUserDTO vUserDTO) {
        try {
            VUser user = userConverter.toEntity(vUserDTO);
            if (user == null) {
                log.error("用户数据转换失败: {}", vUserDTO);
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR));
            }
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getUsername, user.getUsername());
            if (userService.count(wrapper) > 0) {
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR, "用户已存在"));
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            boolean result = userService.save(user);
            return ResponseEntity.ok(ResponseResult.success(result));
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseResult<Boolean>> updateUser(@Parameter(description = "用户Id") @PathVariable Integer id,
                                                              @Parameter(description = "用户信息") @RequestBody VUserDTO vUserDTO) {
        try {
            VUser user = userConverter.toEntity(vUserDTO);
            if (user == null) {
                log.error("用户数据转换失败: {}", vUserDTO);
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR));
            }
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getId, id);
            if (userService.count(wrapper) <= 0) {
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR, "用户不存在"));
            }
            user.setId(id);
            user.setUsername(null);
            if (StrUtil.isNotBlank(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            boolean result = userService.updateById(user);
            return ResponseEntity.ok(ResponseResult.success(result));
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "删除成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseResult<Boolean>> deleteUser(@PathVariable Integer id) {
        try {
            boolean result = userService.removeById(id);
            return ResponseEntity.ok(ResponseResult.success(result));
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }
}

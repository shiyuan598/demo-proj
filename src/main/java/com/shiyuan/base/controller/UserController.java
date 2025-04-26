package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.entity.converter.UserConverter;
import com.shiyuan.base.entity.dto.VUserDTO;
import com.shiyuan.base.entity.vo.BaseResponse;
import com.shiyuan.base.entity.vo.user.VUserVO;
import com.shiyuan.base.entity.vo.user.VUserVOListResponse;
import com.shiyuan.base.entity.vo.user.VUserVOPageResponse;
import com.shiyuan.base.service.VUserService;
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
    public ResponseEntity<ResponseUtils> list() {
        try {
            List<VUser> userList = userService.list();
            return ResponseUtils.success(userConverter.toVOList(userList));
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "司机列表")
    @GetMapping("/driver")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VUserVO.class)))
    public ResponseEntity<ResponseUtils> getDrivers(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry) {
        try {
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getRole, 2);
            if (StrUtil.isNotBlank(blurry)) {
                wrapper.like(VUser::getName, blurry).or().like(VUser::getUsername, blurry);
            }
            List<VUser> drivers = userService.list(wrapper);
            return ResponseUtils.success(userConverter.toVOList(drivers));
        } catch (Exception e) {
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "用户分页列表")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VUserVOPageResponse.class)))
    public ResponseEntity<ResponseUtils> getUserPage(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry,

                                                     @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") long currentPage,

                                                     @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") long pageSize,

                                                     @Parameter(description = "排序字段") @RequestParam(required = false) String sort,

                                                     @Parameter(description = "排序方向 (asc/desc)") @RequestParam(required = false) String order) {

        try {
            IPage<VUserVO> pageVO = userService.getUserPage(blurry, currentPage, pageSize, sort, order);
            return ResponseUtils.success(pageVO);
        } catch (Exception e) {
            log.error("查询用户分页数据异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "添加用户")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "添加成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseUtils> addUser(@Parameter(description = "用户信息") @RequestBody VUserDTO vUserDTO) {
        try {
            VUser user = userConverter.toEntity(vUserDTO);
            if (user == null) {
                log.error("用户数据转换失败: {}", vUserDTO);
                return ResponseUtils.fail(400, "用户数据转换失败");
            }
            // 检查用户名是否已存在
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getUsername, user.getUsername());
            long count = userService.count(wrapper);
            if (count > 0) {
                return ResponseUtils.fail(400, "用户名已存在");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码
            boolean result = userService.save(user);
            return ResponseUtils.success(result);
        } catch (Exception e) {
            log.error("创建用户异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseUtils> updateUser(@Parameter(description = "用户Id") @PathVariable Integer id, @Parameter(description = "用户信息") @RequestBody VUserDTO vUserDTO) {
        try {
            VUser user = userConverter.toEntity(vUserDTO);
            if (user == null) {
                log.error("用户数据转换失败: {}", vUserDTO);
                return ResponseUtils.fail(400, "用户数据转换失败");
            }
            // 检查用户名是否已存在
            LambdaQueryWrapper<VUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(VUser::getId, id);
            long count = userService.count(wrapper);
            if (count <= 0) {
                return ResponseUtils.fail(400, "用户不已存在");
            }
            user.setId(id);
            // 不更新username
            user.setUsername(null);
            // 加密密码
            if (StrUtil.isNotBlank(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码
            }
            boolean result = userService.updateById(user);
            return ResponseUtils.success(result);
        } catch (Exception e) {
            log.error("更新用户异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "删除成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<ResponseUtils> deleteUser(@PathVariable Integer id) {
        try {
            boolean result = userService.removeById(id);
            return ResponseUtils.success(result);
        } catch (Exception e) {
            log.error("删除用户异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }
}

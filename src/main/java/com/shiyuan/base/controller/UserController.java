package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.entity.VUser;
import com.shiyuan.base.entity.converter.UserConverter;
import com.shiyuan.base.entity.vo.user.VUserVO;
import com.shiyuan.base.entity.vo.user.VUserVOListResponse;
import com.shiyuan.base.entity.vo.user.VUserVOPageResponse;
import com.shiyuan.base.service.VUserService;
import com.shiyuan.base.util.PageConverter;
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

    @Operation(summary = "用户分页列表")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VUserVOPageResponse.class)))
    public ResponseEntity<ResponseUtils> getUserPage(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry,

                                                     @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") long currentPage,

                                                     @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") long pageSize,

                                                     @Parameter(description = "排序字段") @RequestParam(required = false) String sort,

                                                     @Parameter(description = "排序方向 (asc/desc)") @RequestParam(required = false) String order) {

        try {
            LambdaQueryWrapper<VUser> queryWrapper = new LambdaQueryWrapper<>();
            if (StrUtil.isNotBlank(blurry)) {
                queryWrapper.like(VUser::getName, blurry).or().like(VUser::getUsername, blurry);
            }
            if (StrUtil.isNotBlank(sort)) {
                boolean isAsc = StringUtils.isBlank(order) || "asc".equalsIgnoreCase(order);
                // 根据不同字段排序
                switch (sort.toLowerCase()) {
                    case "username":
                        queryWrapper.orderBy(true, isAsc, VUser::getUsername);
                        break;
                    case "role":
                        queryWrapper.orderBy(true, isAsc, VUser::getRole);
                        break;
                    case "name":
                        queryWrapper.orderBy(true, isAsc, VUser::getName);
                        break;
                    default:
                        // 默认按id降序
                        queryWrapper.orderByDesc(VUser::getId);
                }
            } else {
                // 默认按id降序
                queryWrapper.orderByDesc(VUser::getId);
            }

            IPage<VUser> userPage = userService.page(new Page<>(currentPage, pageSize), queryWrapper);
            IPage<VUserVO> pageVO = PageConverter.convert(userPage, userConverter::toVO);
            return ResponseUtils.success(pageVO);
        } catch (Exception e) {
            log.error("查询用户分页数据异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }
}

package com.shiyuan.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.shiyuan.base.entity.AppUser;
import com.shiyuan.base.entity.vo.user.AppUserListResponse;
import com.shiyuan.base.entity.vo.user.AppUserPageResponse;
import com.shiyuan.base.entity.vo.user.AppUserVO;
import com.shiyuan.base.service.UserService;
import com.shiyuan.base.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "用户相关")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserListResponse.class)))
    public ResponseEntity<ResponseUtils> list() {
        try {
            List<AppUser> listData = userService.list();
            List<AppUserVO> userVOList = new ArrayList<>();
            for (AppUser user : listData) {
                AppUserVO userVO = new AppUserVO();
                BeanUtils.copyProperties(user, userVO);
                userVOList.add(userVO);
            }
            return ResponseUtils.success(userVOList);
        } catch (Exception e) {
            return ResponseUtils.error("An error occurred while fetching user data: " + e.getMessage());
        }
    }


    @Operation(summary = "用户分页列表")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserPageResponse.class)))
    public ResponseEntity<ResponseUtils> getUserPage(@Parameter(description = "模糊搜索关键字") @RequestParam(required = false) String blurry,

                                                     @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") long currentPage,

                                                     @Parameter(description = "每页条数", example = "10") @RequestParam(defaultValue = "10") long pageSize,

                                                     @Parameter(description = "排序字段") @RequestParam(required = false) String sort,

                                                     @Parameter(description = "排序方向 (asc/desc)") @RequestParam(required = false) String order) {

        try {
            LambdaQueryWrapper<AppUser> queryWrapper = new LambdaQueryWrapper<>();
            if (StrUtil.isNotBlank(blurry)) {
                queryWrapper.like(AppUser::getName, blurry).or().like(AppUser::getUsername, blurry);
            }
            if (StrUtil.isNotBlank(sort)) {
                boolean isAsc = StringUtils.isBlank(order) || "asc".equalsIgnoreCase(order);
                // 根据不同字段排序
                switch (sort.toLowerCase()) {
                    case "username":
                        queryWrapper.orderBy(true, isAsc, AppUser::getUsername);
                        break;
                    case "role":
                        queryWrapper.orderBy(true, isAsc, AppUser::getRole);
                        break;
                    case "name":
                        queryWrapper.orderBy(true, isAsc, AppUser::getName);
                        break;
                    default:
                        // 默认按id降序
                        queryWrapper.orderByDesc(AppUser::getId);
                }
            } else {
                // 默认按id降序
                queryWrapper.orderByDesc(AppUser::getId);
            }

            IPage<AppUser> userPage = userService.page(new Page<>(currentPage, pageSize), queryWrapper);
            List<AppUser> userList = userPage.getRecords();
            List<AppUserVO> userVOList = new ArrayList<>();
            for (AppUser user : userList) {
                AppUserVO userVO = new AppUserVO();
                BeanUtils.copyProperties(user, userVO);
                userVOList.add(userVO);
            }
            Page<AppUserVO> pageVO = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
            pageVO.setRecords(userVOList);
            return ResponseUtils.success(pageVO);
        } catch (Exception e) {
            return ResponseUtils.error("An error occurred while fetching user data: " + e.getMessage());
        }
    }
}

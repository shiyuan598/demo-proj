package com.shiyuan.base.controller;

import com.shiyuan.base.entity.AppUser;
import com.shiyuan.base.util.ResponseUtils;
import com.shiyuan.base.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户相关")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUser.class)))
    public ResponseEntity<ResponseUtils> list() {
        try {
                List<AppUser> listData = userService.list();
                return ResponseUtils.success(listData);
            } catch (Exception e) {
                return ResponseUtils.error("An error occurred while fetching user data: " + e.getMessage());
            }
    }
}

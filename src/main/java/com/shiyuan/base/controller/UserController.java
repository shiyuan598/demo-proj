package com.shiyuan.base.controller;

import com.shiyuan.base.common.ResponseUtil;
import com.shiyuan.base.entity.User;
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
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    public ResponseEntity<ResponseUtil> list() {
        try {
                List<User> listData = userService.list();
                return ResponseUtil.success(listData);
            } catch (Exception e) {
                return ResponseUtil.error("An error occurred while fetching user data: " + e.getMessage());
            }
    }
}

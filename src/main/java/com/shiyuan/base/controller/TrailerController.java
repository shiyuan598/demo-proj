package com.shiyuan.base.controller;

import com.shiyuan.base.common.ResponseUtil;
import com.shiyuan.base.entity.Trailer;
import com.shiyuan.base.service.TrailerService;
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

@Tag(name = "车辆挂载")
@RestController
@RequestMapping("/trailer")
@ApiResponse(responseCode = "200", description = "查询成功",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Trailer.class)))
public class TrailerController {
    @Autowired
    private TrailerService trailerService;

    @Operation(summary = "车辆挂载字典")
    @GetMapping("/dict")
    public ResponseEntity<ResponseUtil> dict() {
        try {
            List<Trailer> listData = trailerService.list();
            return ResponseUtil.success(listData);
        } catch (Exception e) {
            return ResponseUtil.error("An error occurred while fetching trailer data: " + e.getMessage());
        }
    }
}

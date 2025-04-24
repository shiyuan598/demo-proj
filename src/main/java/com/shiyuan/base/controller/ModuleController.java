package com.shiyuan.base.controller;

import com.shiyuan.base.entity.VModule;
import com.shiyuan.base.service.VModuleService;
import com.shiyuan.base.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="所属模块")
@RestController
@RequestMapping("/module")
@Slf4j
public class ModuleController {
    @Autowired
    private VModuleService moduleService;

    @Operation(summary = "所属模块")
    @GetMapping("/dict")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VModule.class)))
    public ResponseEntity<ResponseUtils> dict() {
        try {
            List<VModule> list = moduleService.list();
            return ResponseUtils.success(list);
        } catch (Exception e) {
            log.error("查询所属模块异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }
}

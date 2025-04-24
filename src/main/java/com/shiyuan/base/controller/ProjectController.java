package com.shiyuan.base.controller;

import com.shiyuan.base.entity.VProject;
import com.shiyuan.base.service.VProjectService;
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

@Tag(name="所属项目")
@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController {
    @Autowired
    private VProjectService projectService;

    @Operation(summary = "所属项目")
    @GetMapping("/dict")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VProject.class)))
    public ResponseEntity<ResponseUtils> dict() {
        try {
            List<VProject> list = projectService.list();
            return ResponseUtils.success(list);
        } catch (Exception e) {
            log.error("查询所属项目异常: {}", e.getMessage(), e);
            return ResponseUtils.error(e.getMessage());
        }
    }
}

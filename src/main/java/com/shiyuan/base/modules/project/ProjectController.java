package com.shiyuan.base.modules.project;

import com.shiyuan.base.common.response.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "所属项目")
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private VProjectService projectService;

    @Operation(summary = "所属项目")
    @GetMapping("/dict")
    @ApiResponse(responseCode = "200", description = "查询成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = VProject.class)))
    public ResponseEntity<ResponseResult<List<VProject>>> dict() {
        List<VProject> list = projectService.list();
        return ResponseEntity.ok(ResponseResult.success(list));
    }
}

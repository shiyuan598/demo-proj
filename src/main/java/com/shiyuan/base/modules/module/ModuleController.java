package com.shiyuan.base.modules.module;

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

@Tag(name = "所属模块")
@RestController
@RequestMapping("/module")
public class ModuleController {
    @Autowired
    private VModuleService moduleService;

    @Operation(summary = "所属模块")
    @GetMapping("/dict")
    @ApiResponse(responseCode = "200", description = "查询成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = VModule.class)))
    public ResponseEntity<ResponseResult<List<VModule>>> dict() {
        List<VModule> list = moduleService.list();
        return ResponseEntity.ok(ResponseResult.success(list));
    }
}

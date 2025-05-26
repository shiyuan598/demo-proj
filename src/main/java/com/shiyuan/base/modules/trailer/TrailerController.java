package com.shiyuan.base.modules.trailer;

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

@Tag(name = "车辆挂载")
@RestController
@RequestMapping("/trailer")
public class TrailerController {
    @Autowired
    private VTrailerService trailerService;

    @Operation(summary = "车辆挂载字典")
    @GetMapping("/dict")
    @ApiResponse(responseCode = "200", description = "查询成功",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = VTrailer.class)))
    public ResponseEntity<ResponseResult<List<VTrailer>>> dict() {
        List<VTrailer> listData = trailerService.list();
        return ResponseEntity.ok(ResponseResult.success(listData));
    }
}

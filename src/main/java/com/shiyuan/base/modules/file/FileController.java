package com.shiyuan.base.modules.file;

import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.common.response.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件操作")
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public ResponseEntity<ResponseResult<String>>
        upload(@Parameter(description = "上传文件") @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR));
        }

        try {
            // 使用 UUID 生成唯一文件名
            String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + filename);

            // 创建文件目录
            Files.createDirectories(path.getParent());

            // 文件类型验证（比如只允许图片上传）
            String contentType = file.getContentType();
            if (!isValidFileType(contentType)) {
                return ResponseEntity.badRequest().body(ResponseResult.fail(ResultCode.PARAM_ERROR, "文件类型错误"));
            }

            // 保存文件
            Files.copy(file.getInputStream(), path);

            return ResponseEntity.ok(ResponseResult.success(filename));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "下载文件")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "文件名") @RequestParam String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Path path = Paths.get(UPLOAD_DIR + filename);
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 根据文件后缀设置文件类型
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // 默认二进制流
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 校验文件类型，允许上传的文件类型可以根据实际需要调整
    private boolean isValidFileType(String contentType) {
        return contentType != null && (contentType.startsWith("image/") || contentType.equals("application/pdf"));
    }
}

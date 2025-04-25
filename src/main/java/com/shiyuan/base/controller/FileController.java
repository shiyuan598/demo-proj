package com.shiyuan.base.controller;

import com.shiyuan.base.util.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag(name="文件")
@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/upload")
    public ResponseEntity<ResponseUtils> upload(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + filename);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path);
            return ResponseUtils.success(filename);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String filename) throws IOException {
        try {
            Path path = Paths.get("uploads/" + filename);
            if (!Files.exists(path)) {
                throw new FileNotFoundException("文件不存在: " + filename);
            }
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }
}

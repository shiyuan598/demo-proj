package com.shiyuan.base.modules.file;

import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.common.response.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "文件操作")
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String TEMP_DIR = "temp/";

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

    @Operation(summary = "分片上传文件")
    @PostMapping("/uploadChunk")
    public ResponseEntity<ResponseResult<String>> uploadChunk(
        @Parameter(description = "上传文件") @RequestParam("file") MultipartFile file,
        @Parameter(description = "当前分片索引") @RequestParam("chunkIndex") int index,
        @Parameter(description = "文件 MD5 校验码") @RequestParam("md5") String md5
    ) {
        try {
            Path dir = Paths.get(TEMP_DIR + md5);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Path chunkPath = dir.resolve(index + ".part");
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, chunkPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return ResponseEntity.ok(ResponseResult.success("上传第" + index + "片成功"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "合并分片文件")
    @PostMapping("/mergeChunks")
    public ResponseEntity<?> mergeChunks(
        @Parameter(description = "文件名") @RequestParam("fileName") String fileName,
        @Parameter(description = "文件总分片数") @RequestParam("totalChunks") int totalChunks,
        @Parameter(description = "文件 MD5 校验码") @RequestParam("md5") String md5
    ) {
        try {
            Path dir = Paths.get(TEMP_DIR + md5);
            Path mergedFile = Paths.get(UPLOAD_DIR + fileName);
            if (!Files.exists(mergedFile)) {
                Files.createDirectories(mergedFile.getParent());
            }

            try (RandomAccessFile raf = new RandomAccessFile(mergedFile.toFile(), "rw")) {
                byte[] buffer = new byte[6 * 1024 * 1024];
                for (int i = 0; i < totalChunks; i++) {
                    Path chunk = dir.resolve(i + ".part");
                    try(RandomAccessFile chunkRaf = new RandomAccessFile(chunk.toFile(), "r")) {
                        int readLen = 0;
                        while ((readLen = chunkRaf.read(buffer)) != -1) {
                            raf.write(buffer, 0, readLen);
                        }
                    }
                }
            }
            // 合并完成后清理临时分片
            FileSystemUtils.deleteRecursively(dir);
            return ResponseEntity.ok(ResponseResult.success("合并文件成功" + fileName));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "下载文件")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "文件名") @RequestParam String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            if (!Files.exists(path)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String contentType = URLConnection.guessContentTypeFromName(fileName);
            if (contentType == null) {
                contentType = Files.probeContentType(path);
            }
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @Operation(summary = "获取文件信息")
    @GetMapping("/fileInfo")
    public ResponseEntity<ResponseResult<Map<String, Object>>> getFileInfo(@Parameter(description = "文件名")@RequestParam String fileName) {
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        try {
            long size = Files.size(filePath);
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", fileName);
            result.put("size", size);
            return ResponseEntity.ok(ResponseResult.success(result));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ResponseResult.error(ResultCode.INTERNAL_SERVER_ERROR));
        }
    }

    @Operation(summary = "分块下载文件")
    @GetMapping("/downloadChunk")
    public ResponseEntity<Resource> downloadChunk(
        @Parameter(description = "文件名") @RequestParam String fileName,
        @Parameter(description = "开始位置") @RequestHeader(value = "Range", required = false) String rangeHeader) {

        if (fileName == null || fileName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            File file = filePath.toFile();
            long fileLength = file.length();

            // 无 Range 头，返回整个文件
            if (rangeHeader == null) {
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                    .contentLength(fileLength)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
            }

            // Range: bytes=START-END
            String rangeValue = rangeHeader.trim().toLowerCase().replace("bytes=", "");
            String[] ranges = rangeValue.split("-");
            long start = Long.parseLong(ranges[0]);
            long end = (ranges.length > 1 && !ranges[1].isEmpty()) ? Long.parseLong(ranges[1]) : fileLength - 1;
            end = Math.min(end, fileLength - 1);
            long contentLength = end - start + 1;

            // 创建 InputStream（封装 RandomAccessFile）
            long finalEnd = end;
            InputStream inputStream = new InputStream() {
                private final RandomAccessFile raf = new RandomAccessFile(file, "r");
                private long pos = start;
                private final long endPos = finalEnd;
                private boolean closed = false;

                {
                    raf.seek(start);
                }

                @Override
                public int read() throws IOException {
                    if (pos > endPos) return -1;
                    int b = raf.read();
                    if (b != -1) pos++;
                    return b;
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    if (pos > endPos) return -1;
                    if (pos + len - 1 > endPos) {
                        len = (int) (endPos - pos + 1);
                    }
                    int readLen = raf.read(b, off, len);
                    if (readLen != -1) pos += readLen;
                    return readLen;
                }

                @Override
                public void close() throws IOException {
                    if (!closed) {
                        closed = true;
                        raf.close();
                    }
                }
            };

            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            headers.add("Accept-Ranges", "bytes");

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .contentLength(contentLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // 校验文件类型，允许上传的文件类型可以根据实际需要调整
    private boolean isValidFileType(String contentType) {
        if (contentType == null) return false;

        return contentType.startsWith("image/") ||             // 所有图片类型：image/png, image/jpeg 等
            contentType.equals("application/pdf") ||        // PDF
            contentType.equals("text/plain") ||             // 文本文件 .txt
            contentType.equals("application/zip") ||        // .zip 压缩包
            contentType.equals("application/x-zip-compressed") || // IE 上传 zip 时的类型
            contentType.equals("application/x-rar-compressed") || // RAR 文件
            contentType.equals("application/x-7z-compressed") ||  // 7z 文件
            contentType.equals("application/octet-stream");       // 通用二进制流，某些浏览器上传未知文件类型时会是这个
    }
}

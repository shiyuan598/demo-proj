package com.shiyuan.base.common.log.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLogEntry {
    private String username;
    private String method;
    private String description;
    private String params;
    private String ip;
    private long timeTaken;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

package com.shiyuan.base.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通用响应对象")
public class BaseResponse<T> {
    @Schema(description = "请求是否成功")
    private Boolean success;

    @Schema(description = "数据")
    private T data;
}
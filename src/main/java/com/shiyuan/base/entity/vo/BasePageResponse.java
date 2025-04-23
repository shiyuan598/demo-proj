package com.shiyuan.base.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "通用分页响应对象")
public class BasePageResponse<T> {
    @Schema(description = "请求是否成功")
    private Boolean success;

    @Schema(description = "数据列表")
    private List<T> data;

    @Schema(description = "分页信息")
    private Pagination pagination;

    @Data
    @Schema(description = "分页信息对象")
    public static class Pagination {
        @Schema(description = "总记录数")
        private Long total;

        @Schema(description = "每页记录数")
        private Long pageSize;

        @Schema(description = "当前页码")
        private Long current;

        @Schema(description = "总页数")
        private Long pages;
    }
}
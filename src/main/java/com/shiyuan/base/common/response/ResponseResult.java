package com.shiyuan.base.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@Schema(description = "响应结果封装")
public class ResponseResult<T> {
    @Schema(description = "成功标识")
    private boolean success;

    @Schema(description = "响应状态码")
    private int code;

    @Schema(description = "响应信息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "分页信息")
    private Map<String, Object> pagination;

    private ResponseResult(boolean success, int code, String message, T data, Map<String, Object> pagination) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, null);
    }

    public static <T> ResponseResult<List<T>> success(IPage<T> page) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("current", page.getCurrent());
        pagination.put("size", page.getSize());
        pagination.put("total", page.getTotal());
        pagination.put("pages", page.getPages());
        return new ResponseResult<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(),
            page.getRecords(), pagination);
    }

    public static <T> ResponseResult<T> fail(ResultCode resultCode) {
        return new ResponseResult<>(false, resultCode.getCode(), resultCode.getMessage(), null, null);
    }

    public static <T> ResponseResult<T> fail(ResultCode resultCode, String message) {
        String finalMessage = (message != null) ? message : resultCode.getMessage();
        return new ResponseResult<>(false, resultCode.getCode(), finalMessage, null, null);
    }

    public static <T> ResponseResult<T> error(ResultCode resultCode) {
        return new ResponseResult<>(false, resultCode.getCode(), resultCode.getMessage(), null, null);
    }
}

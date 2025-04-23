package com.shiyuan.base.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseUtil {
    private boolean success;
    private int code;
    private String message;
    private Object data;
    private Map<String, Object> pagination;

    private ResponseUtil(boolean success, int code, String message, Object data, Map<String, Object> pagination) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public static ResponseEntity<ResponseUtil> success(Object data) {
        Map<String, Object> pagination = null;
        if (data instanceof IPage<?> page) {
            pagination = new HashMap<>();
            pagination.put("total", page.getTotal());
            pagination.put("pageSize", page.getSize());
            pagination.put("current", page.getCurrent());
            pagination.put("pages", page.getPages());
            data = page.getRecords();
        }
        return ResponseEntity.ok(new ResponseUtil(true, 0, "success", data, pagination));
    }

    public static ResponseEntity<ResponseUtil> fail(int code, String message) {
        return ResponseEntity.ok(new ResponseUtil(false, code, message, null, null));
    }

    public static ResponseEntity<ResponseUtil> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseUtil(false, 500, message, null, null));
    }
}
package com.shiyuan.base.exception;


import com.shiyuan.base.util.ResponseUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseUtils> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseUtils.fail(400, "参数验证失败: " + errorMessage);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseUtils> handleBindExceptions(BindException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseUtils.fail(400, "参数绑定失败: " + errorMessage);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseUtils> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return ResponseUtils.fail(400, "参数校验失败: " + errorMessage);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseUtils> handleBadRequestException(BadRequestException ex) {
        return ResponseUtils.fail(400, ex.getMessage());
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseUtils> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseUtils.fail(401, ex.getMessage());
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseUtils> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseUtils.fail(404, ex.getMessage());
    }

    /**
     * 处理所有其他未明确处理的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseUtils> handleAllExceptions(Exception ex) {
        return ResponseUtils.error("服务器内部错误: " + ex.getMessage());
    }
}
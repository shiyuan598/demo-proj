package com.shiyuan.base.common.exception;

import com.shiyuan.base.common.response.ResponseResult;
import com.shiyuan.base.common.response.ResultCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 全局异常处理器 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String GENERIC_SERVER_ERROR = "服务器正在努力恢复中，请稍后再试～";
    private static final org.slf4j.Logger operationLogger = org.slf4j.LoggerFactory.getLogger("OPERATION");
    private static final org.slf4j.Logger exceptionLogger = org.slf4j.LoggerFactory.getLogger("EXCEPTION");

    private ResponseEntity<ResponseResult<Object>> buildError(HttpStatus status, ResultCode code, String message,
        Exception e) {
        if (status.is5xxServerError()) {
            // 系统错误：打堆栈记录在 exception.log
            exceptionLogger.error("系统异常: {}, 异常类: {}, 请求失败代码: {}", message, e.getClass().getSimpleName(), code.name(),
                e);
            // 操作审计：不打堆栈，仅关键信息
            operationLogger.error("系统异常: {}, 异常类: {}, 请求失败代码: {}", message, e.getClass().getSimpleName(), code.name());
        } else {
            // 参数错误类的请求异常，不打堆栈
            operationLogger.warn("请求异常: {}, 异常类: {}", message, e.getClass().getSimpleName());
        }

        return ResponseEntity.status(status).body(ResponseResult.fail(code, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseResult<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return buildError(HttpStatus.BAD_REQUEST, ResultCode.PARAM_ERROR, "参数验证失败: " + msg, e);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseResult<Object>> handleBindExceptions(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return buildError(HttpStatus.BAD_REQUEST, ResultCode.PARAM_ERROR, "参数绑定失败: " + msg, e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseResult<Object>> handleConstraintViolationExceptions(ConstraintViolationException e) {
        String msg =
            e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        return buildError(HttpStatus.BAD_REQUEST, ResultCode.PARAM_ERROR, "参数校验失败: " + msg, e);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseResult<Object>> handleBadRequestException(BadRequestException e) {
        return buildError(HttpStatus.BAD_REQUEST, ResultCode.PARAM_ERROR, e.getMessage(), e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseResult<Object>> handleAccessDeniedException(AccessDeniedException e) {
        return buildError(HttpStatus.FORBIDDEN, ResultCode.FORBIDDEN, "无权限访问，请联系管理员", e);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseResult<Object>> handleAuthenticationException(AuthenticationException e) {
        return buildError(HttpStatus.UNAUTHORIZED, ResultCode.UNAUTHORIZED, "用户名或密码错误", e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseResult<Object>> handleUnauthorizedException(UnauthorizedException e) {
        return buildError(HttpStatus.UNAUTHORIZED, ResultCode.UNAUTHORIZED, e.getMessage(), e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseResult<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildError(HttpStatus.NOT_FOUND, ResultCode.NOT_FOUND, e.getMessage(), e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseResult<Object>> handleAllExceptions(Exception e) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ResultCode.INTERNAL_SERVER_ERROR, GENERIC_SERVER_ERROR, e);
    }
}

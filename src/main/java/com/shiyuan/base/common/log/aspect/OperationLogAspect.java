package com.shiyuan.base.common.log.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiyuan.base.common.log.annotation.OperationLog;
import com.shiyuan.base.common.log.model.OperationLogEntry;
import com.shiyuan.base.common.security.SecurityUtils;
import com.shiyuan.base.common.utils.IpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger("OPERATION");

    private final ObjectMapper objectMapper;

    public OperationLogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(opLog)")
    public Object logOperation(ProceedingJoinPoint joinPoint, OperationLog opLog) throws Throwable {
        long start = System.currentTimeMillis();

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("操作失败: {}", ex.getMessage());
            throw ex;
        }

        long duration = System.currentTimeMillis() - start;

        OperationLogEntry entry = new OperationLogEntry();
        entry.setDescription(opLog.value());
        entry.setMethod(joinPoint.getSignature().toShortString());
        entry.setParams(Arrays.toString(joinPoint.getArgs()));
        entry.setTimeTaken(duration);
        entry.setUsername(SecurityUtils.getUsername());
        entry.setIp(IpUtils.getClientIp());
        entry.setTimestamp(LocalDateTime.now());

        // ✅ 使用注入的 ObjectMapper，而不是 new
        log.info(objectMapper.writeValueAsString(entry));

        return result;
    }
}

package com.bruce.debezium.component;

import com.bruce.common.annotation.TraceLog;
import com.bruce.common.constant.StringConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Order(0)
@Aspect
@Component
public class TraceLogAspect {

    @Around("@annotation(traceLog)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, TraceLog traceLog) {
        try {
            Map<String, String> map = MDC.getCopyOfContextMap();
            MDC.setContextMap(map);
            String traceId = MDC.get(StringConst.TRACE_ID);
            if (StringUtils.isBlank(traceId)) {
                traceId = UUID.randomUUID().toString();
                MDC.put(StringConst.TRACE_ID, traceId);
            }
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            MDC.clear();
        }
    }
}

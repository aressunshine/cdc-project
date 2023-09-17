package com.bruce.common.config;

import com.bruce.common.constant.StringConst;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;
import java.util.UUID;

/**
 * 异步方法的日志打印traceId
 */
public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(map);
                String traceId = MDC.get(StringConst.TRACE_ID);
                if (StringUtils.isBlank(traceId)) {
                    traceId = UUID.randomUUID().toString();
                    MDC.put(StringConst.TRACE_ID, traceId);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}

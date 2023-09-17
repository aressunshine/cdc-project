package com.bruce.common.annotation;

import java.lang.annotation.*;

/**
 * 日志链路标记
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface TraceLog {
}

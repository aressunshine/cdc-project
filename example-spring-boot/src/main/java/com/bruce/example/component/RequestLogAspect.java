package com.bruce.example.component;

import com.alibaba.fastjson2.JSON;
import com.bruce.example.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 请求日志切面类
 */
@Slf4j
@Order(1)
@Aspect
@Component
public class RequestLogAspect {

    @Pointcut("execution (* com.bruce.example.controller.*.*(..))")
    public void logPoint() {

    }

    @Around("logPoint()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        HttpServletRequest request = ServletUtils.getRequest();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        Object[] args = proceedingJoinPoint.getArgs();
        log.info("进入地址: {}，请求方式：{}， 请求参数: {}", requestURI, method, JSON.toJSONString(args));
        Object object;
        try {
            object = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        log.info("进入地址: {}，请求方式：{}， 响应参数: {}", requestURI, method, JSON.toJSONString(object));
        return object;
    }
}

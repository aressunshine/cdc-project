package com.bruce.debezium.constant;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public enum ThreadPoolEnum {
    /**
     * 实例
     */
    INSTANCE;

    public static final String DEBEZIUM_LISTENER_POOL = "debezium-listener-pool";
    /**
     * 线程池单例
     */
    private final ExecutorService executorService;

    /**
     * 枚举 (构造器默认为私有）
     */
    ThreadPoolEnum() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(DEBEZIUM_LISTENER_POOL + "-%d").build();
        executorService = new ThreadPoolExecutor(
                8,
                16,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(256),
                threadFactory,
                new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * 公有方法
     *
     * @return ExecutorService
     */
    public ExecutorService getInstance() {
        return executorService;
    }
}

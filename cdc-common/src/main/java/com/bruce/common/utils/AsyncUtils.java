package com.bruce.common.utils;

import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步工具类
 */
public class AsyncUtils {
    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = SpringContextUtils.getBean("threadPoolTaskExecutor");

    /**
     * 操作延迟10毫秒
     */
    private static final int DELAY_TIME = 10;

    private static AsyncUtils instance = new AsyncUtils();

    private AsyncUtils() {
    }

    public static AsyncUtils getInstance() {
        return instance;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        if (Objects.isNull(task)) {
            return;
        }
        executor.schedule(task, DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    public void execute(TimerTask task, int delayTime) {
        if (Objects.isNull(task)) {
            return;
        }
        executor.schedule(task, delayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }
}

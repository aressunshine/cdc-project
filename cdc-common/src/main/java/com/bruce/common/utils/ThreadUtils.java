package com.bruce.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 线程工具类
 */
public class ThreadUtils {

    private static final Logger log = LoggerFactory.getLogger(ThreadUtils.class);

    /**
     * 停止线程池。先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍然超时，则強制退出.
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                        log.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 打印线程异常信息
     *
     * @param runnable  runnable
     * @param throwable throwable
     */
    public static void printException(Runnable runnable, Throwable throwable) {
        if (Objects.isNull(throwable) && (runnable instanceof Future<?> future)) {
            try {
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException exception) {
                throwable = exception;
            } catch (ExecutionException exception) {
                throwable = exception.getCause();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
        if (throwable != null) {
            log.error(throwable.getMessage(), throwable);
        }
    }
}

package com.lyq.transfer.netty.service;

import com.lyq.transfer.util.ThreadFactoryUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * created by lyq
 */
public class CommonThreadService {

    private static final ThreadPoolExecutor commonThreads;

    private static final ScheduledExecutorService scheduledThreads;

    static {
        commonThreads  = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20000),
                (r) -> ThreadFactoryUtil.createThread("commonThreads", r),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        scheduledThreads = new ScheduledThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                (r) -> ThreadFactoryUtil.createThread("scheduledThreads", r),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static void submitTask(Runnable runnable){
        commonThreads.execute(runnable);

    }


    public static void submitTaskDelay(Runnable runnable, long delay, TimeUnit unit){
        scheduledThreads.schedule(runnable, delay, unit);
    }

    public static void submitTaskDefaultDelay(Runnable runnable){
        submitTaskDelay(runnable, 10, TimeUnit.SECONDS);
    }
}

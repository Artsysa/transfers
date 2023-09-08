package com.lyq.transfer.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * created by lyq
 */
public class ThreadFactoryUtil {

    private final static AtomicLong index = new AtomicLong(0L);

    public static Thread createThread(String threadName, Runnable runnable){
        long threadId = index.getAndIncrement();
        if (threadId > (Long.MAX_VALUE >> 1)) {
            index.set(threadId >> 1);
        }
        Thread thread = new Thread(runnable);
        thread.setName(String.format("%s - %s", threadName, threadId));
        return thread;
    }
}

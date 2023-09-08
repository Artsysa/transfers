package com.lyq.transfer.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * created by lyq
 */
public class IncrementUtil {

    private static final AtomicLong index = new AtomicLong(0L);

    public static long incrementAndGet(){
        return index.incrementAndGet();
    }
}

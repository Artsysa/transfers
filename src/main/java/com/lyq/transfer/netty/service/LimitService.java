package com.lyq.transfer.netty.service;

import java.util.concurrent.Semaphore;

public class LimitService {

    private static final Semaphore sendFileSemaphore = new Semaphore(128);

    public static void acquireRequest(){
        try {
            sendFileSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void releaseRequest(){
        sendFileSemaphore.release();
    }
}

package com.lyq.transfer.pojo;

import com.lyq.transfer.netty.CommandCallable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * created by lyq
 */
public class ResponseFuture {

    private Command response;

    private Command request;

    private final CountDownLatch countDownLatch;

    private CommandCallable callable;

    private final long createTimeStamp;

    public ResponseFuture(Command requestCommand) {
        createTimeStamp = System.currentTimeMillis();
        countDownLatch = new CountDownLatch(1);
        request = requestCommand;
    }

    public Command getRequest() {
        return request;
    }

    public void setRequest(Command request) {
        this.request = request;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void release(){
        countDownLatch.countDown();
    }

    public void awaitDefaultTimeOut(){
        try {
            countDownLatch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //
            e.printStackTrace();
        }
    }

    public Command getResponse() {
        return response;
    }

    public void setResponse(Command response) {
        this.response = response;
    }

    public CommandCallable getCallable() {
        return callable;
    }

    public void setCallable(CommandCallable callable) {
        this.callable = callable;
    }
}

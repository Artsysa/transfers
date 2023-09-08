package com.lyq.transfer.netty.service;

import com.google.common.collect.Maps;
import com.lyq.transfer.adapter.CommandAdapter;
import com.lyq.transfer.pojo.ResponseFuture;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * created by lyq
 */
public class ResponseFutureManagerService {


    private final static Map<Long, ResponseFuture> futureMap;

    private final static ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    static {
        futureMap = Maps.newConcurrentMap();
        scheduledExecutor.scheduleAtFixedRate(() -> {
            if(futureMap.isEmpty()){
                return;
            }
            futureMap.forEach((key, responseFuture) -> {
                long currentTimeStamp = System.currentTimeMillis();
                long createTimeStamp = responseFuture.getCreateTimeStamp();
                if (currentTimeStamp - createTimeStamp >= 60000) {
                    responseFuture.setResponse(CommandAdapter.buildFailCommand(responseFuture.getRequest()));
                    if (Objects.isNull(responseFuture.getCallable())) {
                        responseFuture.realse();
                    } else {
                        CommonThreadService.submitTask(() -> {
                            responseFuture.getCallable().invoke(responseFuture.getResponse());
                        });
                    }
                    futureMap.remove(key);
                }
            });
        }, 0, 10, TimeUnit.SECONDS);
    }

    public static void addRequestFuture(Long commandId, ResponseFuture responseFuture){
        futureMap.put(commandId, responseFuture);
    }

    public static ResponseFuture getResponseFuture(Long commandId){
        return futureMap.get(commandId);
    }

    public static void removeResponseFuture(Long commandId){
        futureMap.remove(commandId);
    }
}

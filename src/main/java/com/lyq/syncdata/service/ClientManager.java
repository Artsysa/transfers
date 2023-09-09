package com.lyq.syncdata.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by lyq
 */
public class ClientManager {
    private Map<Integer, ChannelHandlerContext> clientMap = new ConcurrentHashMap<>(16);
    private final AtomicInteger clientIdInc = new AtomicInteger(0);

    public void registClient(ChannelHandlerContext channel){
        clientMap.put(clientIdInc.getAndIncrement(), channel);
    }

    public void remove(ChannelHandlerContext channel){
        for (Map.Entry<Integer, ChannelHandlerContext> entry : clientMap.entrySet()) {
            if(channel == entry.getValue()){
                clientMap.remove(entry.getKey());
                break;
            }
        }
    }

    public ChannelHandlerContext getChannelById(Integer id){
        return clientMap.get(id);
    }

    public Map<Integer, ChannelHandlerContext> getClientMap() {
        return clientMap;
    }
}

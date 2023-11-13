package com.lyq.transfer.netty.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.lyq.transfer.pojo.Command;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.Map.Entry;

/**
 * created by lyq
 */
public class ClientManager {
    private final static Map<Long, ChannelHandlerContext> clientMap;

    static {
        clientMap = Maps.newConcurrentMap();
    }

    public static void registerClient(Command command, ChannelHandlerContext ctx){
        clientMap.put(command.getClientUnique(), ctx);
        System.out.println("register---" + command.getClientUnique());
        RemotingServiceWapper.responseSuccess(command, ctx);
    }

    public static ChannelHandlerContext getDefaultClientId(){
        if(clientMap.size() == 1){
            for (Entry<Long, ChannelHandlerContext> longChannelHandlerContextEntry : clientMap.entrySet()) {
                return longChannelHandlerContextEntry.getValue();
            }
        }
        return null;
    }

    public static ChannelHandlerContext getClientChannel(Long clientId){
        return clientMap.get(clientId);
    }

    public static String getCurrentClientInfo(){
        return JSON.toJSONString(clientMap);
    }

    public static void removeClient(Long clientId){
        clientMap.remove(clientId);
    }
}

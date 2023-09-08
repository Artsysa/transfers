package com.lyq.transfer.netty.service;

import com.google.common.collect.Maps;
import com.lyq.transfer.pojo.Command;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

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
        RemotingServiceWapper.responseSuccess(command);
    }

    public static ChannelHandlerContext getClientChannel(Long clientId){
        return clientMap.get(clientId);
    }

    public static void removeClient(Long clientId){
        clientMap.remove(clientId);
    }
}

package com.lyq.syncdata.controller;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.netty.NettyServerConfig;
import com.lyq.syncdata.pojo.CopyData;
import com.lyq.syncdata.pojo.SyncDataCommand;
import com.lyq.syncdata.service.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * created by lyq
 */
@RestController
@RequestMapping("/sync")
public class SyncDataController {

    @Autowired
    NettyServerConfig nettyServerConfig;

    @RequestMapping("/copy")
    public String copySend(String contain, Integer clientId){
        ChannelHandlerContext channel = nettyServerConfig.getClientManager().getChannelById(clientId);
        if(channel == null){
            return "没有找到客户端id";
        }
        CopyData copyData = new CopyData();
        copyData.setContain(contain);
        channel.channel().writeAndFlush(buildCommand(CommandEnum.COPY_DATA,copyData));
        return "数据已发送";
    }

    @RequestMapping("/copyAll")
    public String copySend(String contain) throws InterruptedException, ExecutionException {
        ClientManager clientManager = nettyServerConfig.getClientManager();
        CopyData copyData = new CopyData();
        copyData.setContain(contain);
        SyncDataCommand syncDataCommand = buildCommand(CommandEnum.COPY_DATA, copyData);
        for (Map.Entry<Integer, ChannelHandlerContext> entry : clientManager.getClientMap().entrySet()) {
            entry.getValue().channel().writeAndFlush(syncDataCommand).get();
        }
        return "数据已发送";
    }

    @RequestMapping("/list")
    public String clientList(){
        return JSON.toJSONString(nettyServerConfig.getClientManager().getClientMap());
    }




    public SyncDataCommand buildCommand(CommandEnum commandEnum, Object body){
        SyncDataCommand command = new SyncDataCommand();
        command.setCommandId(-1);
        command.setCode(commandEnum.getCode());
        command.setBody(JSON.toJSONBytes(body));
        command.setLength(command.getBody().length);
        return command;
    }
}

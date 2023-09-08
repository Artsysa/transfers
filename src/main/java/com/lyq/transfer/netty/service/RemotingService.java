package com.lyq.transfer.netty.service;

import com.lyq.transfer.adapter.CommandAdapter;
import com.lyq.transfer.netty.CommandCallable;
import com.lyq.transfer.pojo.Command;
import com.lyq.transfer.pojo.ResponseFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.Objects;

/**
 * created by lyq
 */
public class RemotingService{

    public static void onewayRemoting(Command command){
        writeAndFlushAndSeeWaterLine(command);
    }

    public static void asyncRemoting(Command command, CommandCallable commandCallable, ChannelHandlerContext ctx){
        ResponseFuture responseFuture = new ResponseFuture(command);
        responseFuture.setCallable(commandCallable);
        ResponseFutureManagerService.addRequestFuture(command.getCommandId(), responseFuture);

        writeAndFlushAndSeeWaterLine(command, ctx);
    }

    public static Command syncRemoting(Command command){
        ResponseFuture responseFuture = new ResponseFuture(command);
        ResponseFutureManagerService.addRequestFuture(command.getCommandId(), responseFuture);

        writeAndFlushAndSeeWaterLine(command);

        responseFuture.awaitDefaultTimeOut();

        Command response = responseFuture.getResponse();

        if(Objects.isNull(response)){
            response = CommandAdapter.buildFailCommand(command);
        }

        return response;
    }


    public static void writeAndFlushAndSeeWaterLine(Command command, ChannelHandlerContext ctx){
        if(Objects.isNull(ctx) ||!ctx.channel().isActive() || !ctx.channel().isOpen()){
            return;
        }

        if(!ctx.channel().isWritable()){
            CommonThreadService.submitTaskDefaultDelay(() -> {
                writeAndFlushAndSeeWaterLine(command, ctx);
            });
            return;
        }

        ctx.channel().writeAndFlush(command);
    }

    public static void writeAndFlushAndSeeWaterLine(Command command){
        writeAndFlushAndSeeWaterLine(command, ClientManager.getClientChannel(command.getClientUnique()));
    }
}

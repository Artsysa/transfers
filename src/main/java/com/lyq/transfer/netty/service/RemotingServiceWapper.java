package com.lyq.transfer.netty.service;

import com.lyq.transfer.adapter.CommandAdapter;
import com.lyq.transfer.adapter.ResponseAdapter;
import com.lyq.transfer.constant.CommandConsts;
import com.lyq.transfer.netty.CommandCallable;
import com.lyq.transfer.pojo.Command;
import com.lyq.transfer.pojo.Response;
import com.lyq.transfer.pojo.UploadFile;
import io.netty.channel.ChannelHandlerContext;

/**
 * created by lyq
 */
public class RemotingServiceWapper extends RemotingService{

    public static void responseSuccess(Command clientCommand){
        Response response = ResponseAdapter.success(null);
        Command responseCommand = CommandAdapter.buildResponseCommand(clientCommand, response);
        writeAndFlushAndSeeWaterLine(responseCommand);
    }

    public static void responseSuccess(Command clientCommand, Object responseBody){
        Response response = ResponseAdapter.success(responseBody);
        Command responseCommand = CommandAdapter.buildResponseCommand(clientCommand, response);
        writeAndFlushAndSeeWaterLine(responseCommand);
    }

    public static void responseSuccess(Command clientCommand, Object responseBody, ChannelHandlerContext ctx){
        Response response = ResponseAdapter.success(responseBody);
        Command responseCommand = CommandAdapter.buildResponseCommand(clientCommand, response);
        writeAndFlushAndSeeWaterLine(responseCommand, ctx);
    }

    public static void responseFail(Command clientCommand){
        Response response = ResponseAdapter.fail(null);
        Command responseCommand = CommandAdapter.buildResponseCommand(clientCommand, response);
        writeAndFlushAndSeeWaterLine(responseCommand);
    }

    public static void uploadFile(UploadFile uploadFile, ChannelHandlerContext ctx, CommandCallable commandCallable){
        asyncRemoting(
                CommandAdapter.buildRequestCommand(CommandConsts.UPLOAD_FILE, uploadFile),
                commandCallable,
                ctx
        );
    }
}

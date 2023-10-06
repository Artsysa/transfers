package com.lyq.transfer.netty;

import com.lyq.transfer.constant.CommandConsts;
import com.lyq.transfer.netty.service.ClientManager;
import com.lyq.transfer.netty.service.CommonThreadService;
import com.lyq.transfer.netty.service.ResponseFutureManagerService;
import com.lyq.transfer.pojo.Command;
import com.lyq.transfer.pojo.ResponseFuture;
import com.lyq.transfer.service.DownloadFileService;
import com.lyq.transfer.service.SaveFileService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

/**
 * created by lyq
 */
@ChannelHandler.Sharable
public class CommonHandler extends SimpleChannelInboundHandler<Command> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg) {
        CommandConsts commandByCode = CommandConsts.getCommandByCode(msg.getType());
        switch (commandByCode){
            case COMMAND_RESPONSE:
                handlerResponse(ctx, msg);
                break;
            case COMMAND_REQUEST:
                handlerRequest(ctx, msg);
                break;
            case UNKNOWN:
                break;
            default:
        }
    }


    private void handlerResponse(ChannelHandlerContext ctx, Command command){
        ResponseFuture responseFuture = ResponseFutureManagerService.getResponseFuture(command.getCommandId());

        if(Objects.isNull(responseFuture)){
            //
            return;
        }

        ResponseFutureManagerService.removeResponseFuture(command.getCommandId());

        responseFuture.setResponse(command);

        if(Objects.nonNull(responseFuture.getCallable())){
            CommonThreadService.submitTask(() ->
                    responseFuture.getCallable().invoke(command)
            );
            return;
        }

        responseFuture.release();
    }


    private void handlerRequest(ChannelHandlerContext ctx, Command command){
        CommandConsts commandRequestCode = CommandConsts.getCommandByCode(command.getCode());

        switch (commandRequestCode){
            case REGISTER_CLIENT:
                ClientManager.registerClient(command, ctx);
                break;
            case GET_FILE_INCREMENT:
                SaveFileService.processorFileIncrement(command);
                break;
            case UPLOAD_FILE:
                SaveFileService.saveFile(command);
                break;
            case CLIENT_DOWNLOAD_INCREMENT:
                DownloadFileService.downloadIncrement(command, ctx);
                break;
            case GET_SERVER_INDEX_FILE:
                DownloadFileService.getServerIndexFile(command, ctx);
                break;
            default:
        }
    }

}

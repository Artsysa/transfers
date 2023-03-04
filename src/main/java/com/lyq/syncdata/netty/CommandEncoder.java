package com.lyq.syncdata.netty;

import com.lyq.syncdata.pojo.SyncDataCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * created by lyq
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToByteEncoder<SyncDataCommand> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SyncDataCommand command, ByteBuf byteBuf) throws Exception {
        try{
            byteBuf.writeInt(command.getLength() + 4 + 4);
            byteBuf.writeInt(command.getCode());
            byteBuf.writeInt(command.getCommandId());
            byteBuf.writeBytes(command.getBody());
        }catch (Exception e){
            //
        }
    }
}

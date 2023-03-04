package com.lyq.syncdata.netty;

import com.lyq.syncdata.pojo.SyncDataCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * created by lyq
 */
public class CommandDecoder extends LengthFieldBasedFrameDecoder {
    public CommandDecoder() {
        super(Integer.MAX_VALUE, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            ByteBuffer byteBuffer = frame.nioBuffer();
            int bodyLength = byteBuffer.getInt();
            int code = byteBuffer.getInt();
            int commandId = byteBuffer.getInt();
            byte[] bodyData = new byte[bodyLength - 4 - 4];
            byteBuffer.get(bodyData);

            SyncDataCommand command = new SyncDataCommand();
            command.setCode(code);
            command.setCommandId(commandId);
            command.setBody(bodyData);
            return command;
        } catch (Exception e) {
            //
        } finally {
            if (null != frame) {
                frame.release();
            }
        }
        return null;
    }
}

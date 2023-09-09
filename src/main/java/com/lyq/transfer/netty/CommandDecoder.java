package com.lyq.transfer.netty;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.pojo.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * created by lyq
 */
public class CommandDecoder extends LengthFieldBasedFrameDecoder {


    SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, CommonConsts.symmetry_key.getBytes(StandardCharsets.UTF_8));

    public CommandDecoder() {
        super(Integer.MAX_VALUE, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        ByteBuf frame = null;
        try {
            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }

            ByteBuffer byteBuffer = frame.nioBuffer();
            int bodyLength = byteBuffer.getInt();
            int code = byteBuffer.getInt();
            int type = byteBuffer.getInt();
            long commandId = byteBuffer.getLong();
            long clientUnique = byteBuffer.getLong();
            byte[] bodyData = new byte[bodyLength - 4 - 4 - 8 - 8];
            byteBuffer.get(bodyData);

            Command command = new Command();
            command.setCode(code);
            command.setCommandId(commandId);
            command.setType(type);
            command.setClientUnique(clientUnique);
            command.setBody(aes.decrypt(bodyData));
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

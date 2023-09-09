package com.lyq.transfer.netty;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.pojo.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * created by lyq
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToByteEncoder<Command> {

    SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, CommonConsts.symmetry_key.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command command, ByteBuf byteBuf) {
        try{
            byte[] body = aes.encrypt(command.getBody());

            byteBuf.writeInt(body.length + 4 + 4 + 8 + 8);
            byteBuf.writeInt(command.getCode());
            byteBuf.writeInt(command.getType());
            byteBuf.writeLong(command.getCommandId());
            byteBuf.writeLong(command.getClientUnique());
            byteBuf.writeBytes(body);
        }catch (Exception e){
            //
            e.printStackTrace();
        }
    }
}

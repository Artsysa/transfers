package com.lyq.syncdata.netty;

import com.lyq.syncdata.pojo.SyncDataCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * created by lyq
 */
public interface SyncDataCommandProcessor {

    void processor(ChannelHandlerContext ctx, SyncDataCommand command);

    boolean match(SyncDataCommand command);

    default void unconnection() {

    }
}

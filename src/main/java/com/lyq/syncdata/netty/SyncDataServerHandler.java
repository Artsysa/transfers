package com.lyq.syncdata.netty;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.pojo.SyncDataCommand;
import com.lyq.syncdata.service.BigDataService;
import com.lyq.syncdata.service.ClientManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * created by lyq
 */
@ChannelHandler.Sharable
public class SyncDataServerHandler extends SimpleChannelInboundHandler<SyncDataCommand> {

    private static final Logger log = LoggerFactory.getLogger("SyncDataServerHandler");

    private  List<SyncDataCommandProcessor> syncDataCommandProcessorList;

    private final ThreadPoolExecutor commonThreads;

    private final RequestManager requestManager;

    private final BigDataService bigDataService;

    private final ClientManager clientManager;

    public SyncDataServerHandler(ThreadPoolExecutor commonThreads, RequestManager requestManager, BigDataService bigDataService, ClientManager clientManager) {
        this.commonThreads = commonThreads;
        this.requestManager = requestManager;
        this.bigDataService = bigDataService;
        this.clientManager = clientManager;
        initProcessor();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SyncDataCommand command) throws Exception {
        for (SyncDataCommandProcessor syncDataCommandProcessor : syncDataCommandProcessorList) {
            if(syncDataCommandProcessor.match(command)){
                syncDataCommandProcessor.processor(channelHandlerContext, command);
                return;
            }
        }
        log.error("不支持该指令集, Command:{}", JSON.toJSONString(command));
    }

    public void initProcessor(){
        this.syncDataCommandProcessorList = new ArrayList<>();
        syncDataCommandProcessorList.add(new SaveDataProcessor(commonThreads, bigDataService));
        syncDataCommandProcessorList.add(new CommonCommandProcessor(commonThreads, requestManager));
        syncDataCommandProcessorList.add(new DownLoadFileProcessor(commonThreads, requestManager));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        for (SyncDataCommandProcessor syncDataCommandProcessor : syncDataCommandProcessorList) {
            syncDataCommandProcessor.unconnection();
        }
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        for (SyncDataCommandProcessor syncDataCommandProcessor : syncDataCommandProcessorList) {
            syncDataCommandProcessor.unconnection();
        }
        clientManager.remove(ctx);
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        clientManager.registClient(ctx);
        super.channelRegistered(ctx);
    }
}

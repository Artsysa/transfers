package com.lyq.syncdata.netty;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.pojo.Progress;
import com.lyq.syncdata.pojo.SyncDataCommand;
import com.lyq.syncdata.service.CheckPointService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * created by lyq
 */
public class CommonCommandProcessor implements SyncDataCommandProcessor{

    private static final Logger log = LoggerFactory.getLogger("CommonCommandProcessor");


    private static final CommandEnum progressEnum = CommandEnum.GET_SYNC_PROGRESS;
    private static final CommandEnum serverResponseEnum = CommandEnum.CLIENT_RESPONSE;
    private final ThreadPoolExecutor commonWorks;
    private final CheckPointService checkPointService;
    private final RequestManager requestManager;

    public CommonCommandProcessor(ThreadPoolExecutor commonWorks, RequestManager requestManager) {
        this.commonWorks = commonWorks;
        this.requestManager = requestManager;
        checkPointService = new CheckPointService();
    }

    @Override
    public void processor(ChannelHandlerContext ctx, SyncDataCommand command) {
        switch (Objects.requireNonNull(CommandEnum.getCommandEnum(command.getCode()))){
            case GET_SYNC_PROGRESS :
                commonWorks.execute(() -> {
                    getNewProgress(ctx, command);
                });
            break;
            case CLIENT_RESPONSE :
                Integer commandId = command.getCommandId();
                RequestFuture requestFuture = requestManager.getByCommandId(commandId);
                requestFuture.setResponse(command);
                if(requestFuture.getCommandCallable() != null){
                    requestManager.asyncInvoke(() -> {
                        try {
                            requestFuture.invoke();
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }finally {
                            requestManager.remove(commandId);
                        }
                    });
                }
                break;
            default:
                log.error("不支持该指令,command:{}" + JSON.toJSONString(command));
        }
    }

    @Override
    public boolean match(SyncDataCommand command) {
        return progressEnum.getCode().equals(command.getCode()) || serverResponseEnum.getCode().equals(command.getCode());
    }

    public void getNewProgress(ChannelHandlerContext ctx, SyncDataCommand command){
        Progress progress = checkPointService.getNewProgress();
        SyncDataCommand syncDataCommand = new SyncDataCommand();
        syncDataCommand.setCommandId(command.getCommandId());
        syncDataCommand.setCode(CommandEnum.GET_SYNC_PROGRESS.getCode());
        syncDataCommand.setBody(JSON.toJSONBytes(progress));
        syncDataCommand.setLength(syncDataCommand.getBody().length);
        ctx.writeAndFlush(syncDataCommand);
    }
}

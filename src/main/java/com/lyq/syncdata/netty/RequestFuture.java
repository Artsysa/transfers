package com.lyq.syncdata.netty;

import com.lyq.syncdata.pojo.SyncDataCommand;

import java.io.IOException;

public class RequestFuture {

    private final CommandCallable commandCallable;

    private final SyncDataCommand clientCommand;

    private SyncDataCommand serverCommand;

    private final Long createTime;

    private boolean hasResponse = false;


    public RequestFuture(CommandCallable commandCallable, SyncDataCommand clientCommand) {
        this.commandCallable = commandCallable;
        this.clientCommand = clientCommand;
        this.createTime = System.currentTimeMillis();
    }

    public void invoke() throws IOException {
        if(commandCallable != null){
            commandCallable.invoke(serverCommand);
        }
    }

    public SyncDataCommand getClientCommand() {
        return clientCommand;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public boolean isHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public void setResponse(SyncDataCommand serverCommand){
        this.serverCommand = serverCommand;
        hasResponse = true;
    }

    public CommandCallable getCommandCallable() {
        return commandCallable;
    }
}

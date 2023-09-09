package com.lyq.syncdata.pojo;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.constant.ServerResponseEnum;

/**
 * created by lyq
 */
public class SyncDataCommand {
    private Integer length;
    private byte[] body;
    private Integer code;
    private Integer commandId;

    public Integer getCommandId() {
        return commandId;
    }

    public void setCommandId(Integer commandId) {
        this.commandId = commandId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public static SyncDataCommand buildResponse(SyncDataCommand clientCommand, boolean success){
        SyncDataCommand syncDataCommand = new SyncDataCommand();
        syncDataCommand.setCommandId(clientCommand.getCommandId());
        syncDataCommand.setCode(CommandEnum.SERVER_RESPONSE.getCode());
        ServerResponse serverResponse = new ServerResponse();
        if(success){
            serverResponse.setCode(ServerResponseEnum.SAVE_SUCCESS.getCode());
        }else{
            serverResponse.setCode(ServerResponseEnum.SAVE_ERROR.getCode());
            serverResponse.setDescription(ServerResponseEnum.SAVE_ERROR.getDescription());
        }
        syncDataCommand.setBody(JSON.toJSONBytes(serverResponse));
        syncDataCommand.setLength(syncDataCommand.getBody().length);
        return syncDataCommand;
    }
}

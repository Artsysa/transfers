package com.lyq.transfer.adapter;

import com.alibaba.fastjson.JSON;
import com.lyq.transfer.constant.CommandConsts;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.pojo.Command;

import cn.hutool.core.util.IdUtil;
import com.lyq.transfer.pojo.Response;

import java.util.Objects;

/**
 * created by lyq
 */
public class CommandAdapter {


    public static Command buildResponseCommand(Command clientCommand, Response response){
        clientCommand.setType(CommandConsts.COMMAND_RESPONSE.getCode());
        clientCommand.setBody(JSON.toJSONBytes(response));
        return clientCommand;
    }


    public static Command buildRequestCommand(CommandConsts requestCommand, Object object){
        return buildCommand(CommandConsts.COMMAND_REQUEST, requestCommand, object);
    }

    public static Command buildCommand(CommandConsts requestType, CommandConsts requestCommand, Object object){
        Command command = new Command();
        command.setCommandId(IdUtil.getSnowflakeNextId());
        command.setBody(JSON.toJSONBytes(object));
        command.setType(requestType.getCode());
        command.setClientUnique(CommonConsts.client_unique);
        if(Objects.nonNull(requestCommand)){
            command.setCode(requestCommand.getCode());
        }
        return command;
    }

    public static Command buildFailCommand(Command requestCommand){
        Command command = new Command();
        command.setClientUnique(CommonConsts.client_unique);
        return buildCommand(CommandConsts.COMMAND_RESPONSE, CommandConsts.getCommandByCode(requestCommand.getCode()), ResponseAdapter.fail(null));
    }
}
